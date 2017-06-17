package simple.gui.component;

import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MoveableJList<T> extends JList<T>{
	private static final long serialVersionUID=1L;

	public MoveableJList(){
		super();
	}

	public MoveableJList(DefaultListModel<T> dataModel){
		super(dataModel);
	}

	public MoveableJList(T[] listData){
		super(listData);
	}

	public MoveableJList(Vector<T> listData){
		super(listData);
	}

	public void moveSelectedUp(){
		if(this.getSelectedIndex() == -1){
			return;
		}

		final int[] indices = this.getSelectedIndices();
		if (indices[0] == 0){
			return;
		}

		final DefaultListModel<T> model= (DefaultListModel<T>)this.getModel();
		T tmp= model.getElementAt(indices[0] - 1);
		for (int i= 0; i < indices.length; i++) {
			model.set(indices[i]-1, model.getElementAt(indices[i]));
			indices[i]--;
		}
		model.set(indices[indices.length-1] + 1, tmp);

		// updated selected and scroll to them
		this.setSelectedIndices(indices);
		this.scrollRectToVisible(this.getCellBounds(indices[0], indices[indices.length-1]));
	}

	public void moveSelectedDown(){
		if(this.getSelectedIndex() == -1){
			return;
		}

		final int[] indices = this.getSelectedIndices();
		final DefaultListModel<T> model= (DefaultListModel<T>)this.getModel();
		final int insertIndex = indices[indices.length-1] + 1;
		if (insertIndex == model.size()){
			return;
		}

		T tmp= model.get(insertIndex);
		for (int index= insertIndex; index > indices[0]; index--) {
			model.set(index, model.get(index-1));
		}
		model.set(indices[0], tmp);
		for (int i = 0; i<indices.length; i++) {
			indices[i]++;
		}

		// updated selected and scroll to them
		this.setSelectedIndices(indices);
		this.scrollRectToVisible(this.getCellBounds(indices[0], indices[indices.length-1]));
	}

	@SuppressWarnings("unchecked")
	public void moveSelectedToTop(){
		if(this.getSelectedIndex() == -1){
			return;
		}

		final int[] indices = this.getSelectedIndices();

		if (indices[0] == 0){
			return;
		}

		final DefaultListModel<T> model= (DefaultListModel<T>)this.getModel();
		final Object[] top = new Object[indices[0]];

		for (int i = 0; i < top.length; i++){
			top[i] = model.get(i);
		}

		for (int i = 0; i < indices.length; i++) {
			model.set(i, model.get(indices[i]));
			indices[i] = i;
		}

		final int offset = indices.length;
		for (int i = 0; i < top.length; i++){
			model.set(i + offset, (T)top[i]);
		}

		// updated selected and scroll to them
		this.setSelectedIndices(indices);
		this.scrollRectToVisible(this.getCellBounds(indices[0], indices[indices.length-1]));
	}

	@SuppressWarnings("unchecked")
	public void moveSelectedToBottom(){
		if(this.getSelectedIndex() == -1){
			return;
		}

		final int[] indices = this.getSelectedIndices();
		final DefaultListModel<T> model= (DefaultListModel<T>)this.getModel();

		if (indices[indices.length-1] == model.size()-1){
			return;
		}

		final Object[] bottom = new Object[model.size()-indices[indices.length-1]-1];
		int i;
		int offset = indices[indices.length - 1] + 1;
		for (i= 0; i < bottom.length; i++){
			bottom[i] = model.get(i+offset);
		}

		offset = model.size() - indices.length;
		for (i= 0; i < indices.length; i++) {
			model.set(i + offset, model.get(indices[i]));
			indices[i]= i + offset;
		}

		offset= offset - bottom.length;
		for (i= 0; i < bottom.length; i++){
			model.set(i + offset, (T)bottom[i]);
		}

		// updated selected and scroll to them
		this.setSelectedIndices(indices);
		this.scrollRectToVisible(this.getCellBounds(indices[0], indices[indices.length-1]));
	}

	public void removeSelected(){
		if(this.getSelectedIndex() == -1){
			return;
		}

		final int[] indices = this.getSelectedIndices();
		final DefaultListModel<T> model= (DefaultListModel<T>)this.getModel();

		int offset= 0;
		for(int i : indices){
			model.remove(i - offset);
			offset++;
		}
	}
}
