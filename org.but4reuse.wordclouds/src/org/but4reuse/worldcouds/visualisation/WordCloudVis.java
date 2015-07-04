package org.but4reuse.worldcouds.visualisation;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.wordclouds.ui.actions.WordCloudAction;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * @author Arthur
 *  This class is an eclipse view.
 */

public class WordCloudVis extends ViewPart {

	
	/**
	 * The current WorldCloudVis.
	 */
	static private WordCloudVis singleton = null;
	
	/**
	 *  The combo control in the view. 
	 */
	private Combo combo;
	
	/**
	 * The list control in the view.
	 */
	private List list;
	
	/**
	 * The canvas control where word clouds are drawn.
	 */
	private Canvas canvas;
	
	/**
	 *  The text control in the view
	 */
	private Text text;
	
	/**
	 *  The button named "rename" in the view
	 */
	private Button accept;
	
	/**
	 * The button named "Rename All Auto"
	 */
	private Button renameAll;
	
	/**
	 * The button named "Rename Current Auto"
	 */
	private Button renameOne;
	
	/**
	 *  Default constructor.
	 */
	public WordCloudVis() {
		// TODO Auto-generated constructor stub
		super();
		this.singleton = this;
	}

	/**
	 *  This method returns the value of singleton attribute. 
	 * @return The singleton
	 */
	public static WordCloudVis getSingleton()
	{
		return singleton;
	}
	
	/**
	 * This method returns the combo control from the view. 
	 * @return The combo control
	 */
	public Combo getCombo()
	{
		return combo;
	}
	
	/**
	 * This method returns the list control from the view.
	 * @return The list control
	 */
	public List getList()
	{
		return list;
	}
	
	/**
	 * This method returns the canvas control from the view.
	 * @return the canvas control
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	/**
	 * The method returns the text control from the view
	 * @return the text control
	 */
	public Text getText()
	{
		return text;
	}
	
	/**
	 * This method will update the singleton.
	 * It will set the selected item at index.\n
	 * The method will call WordCloudAction.getClouds() to get
	 * cloud at the index "index" and  fill the list control with
	 * strings contained in the cloud. 
	 * @param index Index must be the index of an item from combo control.
	 * @param redraw To indicate if you want clear the canvas content.
	 */
	public static void update(int index,boolean redraw)
	{
		
		// If features aren't identified, it isn't necessary to update the view
		if(singleton == null ||
				AdaptedModelManager.getAdaptedModel() == null)
			return;
		/*
		 * We clear the content for every control in the view.
		 */
		singleton.getCombo().clearSelection();
		singleton.getCombo().removeAll();
		singleton.getList().removeAll();
		singleton.getText().setText("");
		
		for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
			singleton.getCombo().add(b.getName());
		singleton.getCombo().select(index);
		
		
		GC gc = new GC(singleton.getCanvas());
		
		Cloud c = WordCloudAction.getClouds().get(index);
		for(Tag t : c.tags())
			singleton.getList().add(t.getName());
		
	    WordCloudUtil.drawWordCloud(singleton.getCanvas(), c);
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		grid.marginHeight = 3;
		GridData data;
		
		parent.setLayout(grid);
		
		
		
		list = new List(parent, SWT.BORDER  | SWT.READ_ONLY);
		canvas = new Canvas(parent,SWT.BORDER);
		combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		text = new Text(parent, SWT.BORDER);
		renameOne = new Button(parent, SWT.NORMAL);	
		accept = new Button(parent, SWT.NORMAL);
		renameAll = new Button(parent, SWT.NORMAL);
	
		
		data = new GridData();
		data.heightHint= 400;
		data.widthHint = 500;
		canvas.setLayoutData(data);
		
		data = new GridData();
		data.heightHint= 25;
		data.widthHint = 380;
		combo.setLayoutData(data);
		
		data = new GridData();
		data.heightHint= 400;
		data.widthHint = 400;
		list.setLayoutData(data);
		
		
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   renameOne.setLayoutData(data);
	   renameOne.setText("Rename Current Auto");
		   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   renameAll.setLayoutData(data);
	   renameAll.setText("Rename All Auto");
	   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   text.setLayoutData(data);
	   
	   data = new GridData();
	   data.heightHint = 25;
	   data.widthHint = 150;
	   accept.setLayoutData(data);
	   accept.setText("Rename Block");
	   
	   /*
	    * When you will click on this button you will rename each block
	    * with the most often string saw in the block's word cloud. 
	    *  
	    */
	   renameAll.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					 
					 int ind = 0;
					 for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
					 {
						 
						 Cloud c = WordCloudAction.getClouds().get(ind);
						 if(c.tags().size() == 0)
							 return;
						 
						 Tag last = c.tags().get(0);
						 for(Tag t : c.tags())
						 {
							 if(t.getWeight() > last.getWeight())
								last = t; 
						 }
						 b.setName(last.getName());
						 ind++;
					 }
					WordCloudVis.update(combo.getSelectionIndex(),true);
					break;
					 
				}
			}
		      });
	   
	   /*
	    * Here, we will just rename the selected block with the string
	    *  which is the most often saw in the block' word cloud.
	    */
	   renameOne.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					 int ind = combo.getSelectionIndex();
					 Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					 Cloud c = WordCloudAction.getClouds().get(ind);
					 if(c.tags().size() == 0)
						 return;
					 
					 Tag last = c.tags().get(0);
					 for(Tag t : c.tags())
					 {
						 if(t.getWeight() > last.getWeight())
							last = t; 
					 }
					 b.setName(last.getName());
					 WordCloudVis.update(combo.getSelectionIndex(),true);
					 break;
					 
				}
			}
		      });
	   
	   /*
	    * this one will rename the selected block with the text in the text control.
	    */
	   accept.addListener(SWT.Selection, new Listener() {
		      
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch(event.type)
				{
				 case SWT.Selection:
					
					if(text.getText().equals(""))
						return;
					
					int ind = combo.getSelectionIndex();
					Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(ind);
					b.setName(text.getText());
					WordCloudVis.update(combo.getSelectionIndex(),true);
					break;
					 
				}
			}
		      });
	  
	   /*
	    * When you choose a an item from the combo control it must update the view.
	    * it updates the list control and canvas control. 
	    */
	   combo.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Hello selected");
					Combo c = (Combo)e.getSource();
					WordCloudVis.update(c.getSelectionIndex(),true);
									
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				
				}
			});
	   
	   /*
	    * When you click on an item from the list it will rename the selected
	    * block with the value of the selected item.
	    */
	   list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				List l = (List)e.getSource();
				
				Block b = AdaptedModelManager.getAdaptedModel().getOwnedBlocks().get(combo.getSelectionIndex());
				b.setName(l.getItem(l.getSelectionIndex()));
				WordCloudVis.update(combo.getSelectionIndex(),true);
								
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			
			}
		});
		
			if(AdaptedModelManager.getAdaptedModel() == null)
				return;
			
			for(Block b : AdaptedModelManager.getAdaptedModel().getOwnedBlocks())
				combo.add(b.getName());
			
			if(combo.getItemCount() > 0)
				combo.select(0);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	

}
