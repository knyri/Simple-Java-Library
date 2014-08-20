package simple.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import simple.io.FileUtil;
import simple.io.StreamFactory;
import simple.net.http.clientparams.ClientParam;
import simple.net.http.clientparams.FileParam;
import simple.net.http.clientparams.StringParam;

public class MultipartFormEntity extends AbstractHttpEntity{
	private final ClientParam[] params;
	private final String boundary;
	private byte[] content=null;
	private final Object contentSync=new Object();
	private boolean tooBig=false;
	public MultipartFormEntity(ClientParam[] params,String boundary){
		this.params=params;
		this.boundary=boundary;
	}
	private void generate(){
		ByteArrayOutputStream tmp=new ByteArrayOutputStream(params.length*4192);
		synchronized(contentSync){
			for(int i=0;i<params.length;i++){
				try{
					tmp.write(("--"+boundary+"\r\n").getBytes());
					tmp.write(getParam(i));
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			try{
				tmp.write(("--"+boundary+"--\r\n").getBytes());
			}catch(IOException e){
				e.printStackTrace();
			}
			content=tmp.toByteArray();
		}
	}
	private byte[] getParam(int index){
		ClientParam param=params[index];
		if(param instanceof StringParam){
			return ("Content-Disposition: form-data; name=\""+param.getName()+"\"\r\n\r\n"+param.getValue()+"\r\n").getBytes();
		}else if(param instanceof FileParam){
			File file=new File(param.getValue().replace('/',File.separatorChar));
			ByteArrayOutputStream tmp=new ByteArrayOutputStream(10000000);
			if(!file.exists())throw new IllegalArgumentException("File does not exists! "+file.getAbsolutePath());
			if(file.length()>15000000){
				tooBig=true;
				content=null;
				return null;
			}
			try{
				tmp.write(("Content-Disposition: form-data; name=\""+param.getName()
						+"\"; filename=\""+param.getValue().substring(param.getValue().lastIndexOf('/'))
						+"\"\r\nContent-Type: "+((FileParam)param).getContentType()+"\r\n\r\n").getBytes());
				InputStream in =StreamFactory.getBufferedInputStream(file);
				FileUtil.copy(in,tmp,6000);
				tmp.write("\r\n".getBytes());
				FileUtil.close(in);
			}catch(FileNotFoundException e){
				throw new IllegalArgumentException("File does not exists! "+file,e);
			}
			catch(IOException e){
				throw new RuntimeException(e);
			}
			return tmp.toByteArray();
		}else{
			return null;
		}
	}

	@Override
	public boolean isRepeatable(){return true;}

	@Override
	public long getContentLength(){
		if(!tooBig && content==null)generate();
		if(tooBig)return -1;
		return content.length;
	}

	@Override
	public InputStream getContent() throws IOException,IllegalStateException{
		if(getContentLength()!=-1)
			return new ByteArrayInputStream(content);
		else
			return new ContentStream();
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException{
		//StreamFactory.copy(getContent(),System.out,9000);
		FileUtil.copy(getContent(),outstream,9000);
	}

	@Override
	public boolean isStreaming(){return false;}


	class ContentStream extends InputStream{
		int currentItem=0;
		InputStream in=null;
		int bbyte;
		@Override
		public int read() throws IOException{
			if(in==null && !setupNextRead()) return -1;
			bbyte=in.read();
			if(bbyte==-1 && setupNextRead())
				bbyte=in.read();
			return bbyte;
		}
		private boolean setupNextRead() throws FileNotFoundException{
			if(currentItem==params.length){
				in=new ByteArrayInputStream(("--"+boundary+"--\r\n").getBytes());
				return true;
			}
			if(currentItem==-1)return false;
			currentItem++;
			if(params[currentItem] instanceof FileParam)
				in=new FileStream((FileParam)params[currentItem]);
			else
				in=new ByteArrayInputStream(getParam(currentItem));
			return true;
		}
	}
	class FileStream extends InputStream{
		final byte[] header;
		int offset=0,bbyte;
		final InputStream filestream;
		public FileStream(FileParam param) throws FileNotFoundException{
			File file=new File(param.getValue().replace('/',File.separatorChar));
			String filename=param.getValue();
			if(filename.contains("/"))
				filename=filename.substring(filename.lastIndexOf('/'));
			header=(boundary+"\r\nContent-Disposition: form-data; name=\""+param.getName()
					+"\"; filename=\""+filename
					+"\"\r\nContent-Type: "+(param).getContentType()+"\r\n\r\n").getBytes();
			filestream =StreamFactory.getBufferedInputStream(file);
		}
		@Override
		public int read() throws IOException{
			if(offset<0){
				if(offset==-1){
					offset--;
					return '\n';
				}
				return -1;
			}
			if(offset==header.length){
				bbyte=filestream.read();
				if(bbyte==-1){
					offset=-1;
					return '\r';
				}
				return bbyte;
			}
			return header[offset++];
		}

	}
}
