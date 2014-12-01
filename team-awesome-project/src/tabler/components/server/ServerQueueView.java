package tabler.components.server;

import java.util.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tabler.components.table.*;
import tabler.components.floor.*;
import tabler.components.guest.*;

public class ServerQueueView extends JFrame{
	public JFrame frame;
	public JPanel panel;
	public JList list;
	public DefaultListModel lModel;
	public JScrollPane pane;
	public JLabel label;
	public JButton view;
	public JButton skip;
	public JButton prev;
	public JButton revert;
	public JScrollBar bar;
	public ArrayList<String> servers;
	public String [] possibleValues;
	int visibleIndex;
	/**
	 * ServerQueueView constructor
	 * @param ServerQueueModel model
	 */
	public ServerQueueView (ServerQueueModel model){
		setLayout(new BorderLayout());
		servers = new ArrayList<String>();
		
		this.frame = null;
		int len = model.getSize();

		for(int i = 0; i < len; i++){
			this.servers.add(model.getList().get(i).getServerName());
		}
		
		lModel = new DefaultListModel();
		list = new JList<String>(lModel);
		for(int i = 0; i < len; i ++){
			lModel.addElement(servers.get(i));

		}
		
		JScrollPane pane = new JScrollPane();
		pane.getViewport().setView(list);
		this.visibleIndex = 0;
        this.list.ensureIndexIsVisible(0);
		view = new JButton("view");
		skip = new JButton("skip");
		prev = new JButton("previous");
		revert = new JButton("hide");
		
		pane.setViewportView(list);
		
		label = new JLabel("...");
		
	}
	/**
	 * creates  and displays specific instances of serverQueueView from previous changes 
	 * and initial creation
	 */
	public void createAndShowGui(){
		
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(1);
		list.ensureIndexIsVisible(this.visibleIndex);

		pane = new JScrollPane(list);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		if (this.frame != null){
			this.frame.dispose();
		}
        this.frame = new JFrame("Foo001");
        this.panel = new JPanel(new BorderLayout());
        JPanel subpanel = new JPanel();
        subpanel.add(label);
        subpanel.add(revert);
   
	    panel.add(view, BorderLayout.NORTH);
	    panel.add(skip, BorderLayout.EAST);
	    panel.add(prev, BorderLayout.WEST);
	    panel.add(subpanel, BorderLayout.SOUTH);

        panel.add(pane, BorderLayout.CENTER);
	    frame.add(panel);
        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
   
	}

	/**
	 * editShownList() results in altering the user's view of the serverQueue 
	 * method called each time a server is seated
	 * @param ServerQueueModel model
	 */
	public void editShownList(ServerQueueModel model){
		
		this.servers.clear();
		this.lModel.clear();
		int len = model.getSize();

		for(int i = 0; i < len; i++){
			this.servers.add(model.getList().get(i).getServerName());
		}
		
		this.lModel = new DefaultListModel();
		this.list = new JList<String>(lModel);
		for(int i = 0; i < len; i ++){
			lModel.addElement(servers.get(i));
		}
		this.visibleIndex = 0;
		createAndShowGui();
		
	}
	/**
	 * Returns index of server in activeQueue currently shown
	 * @return int: visibleIndex
	 */
	public int getChosenIndex(){
		return this.visibleIndex;
	}
	/**
	 * skip() method is called in response to user pressing "skip"
	 *  alters visibleIndex- attribute denoting activeQueue 
	 * Index currently shown-increases by 1, creates new gui with updated list view
	 * @param ServerQueueModel: model
	 */
	public void skip(ServerQueueModel model){
		if(model.getSize() == 0){
			System.out.println("Queue is empty");
			return;
		}
		if (this.visibleIndex == model.getSize()-1){
			this.visibleIndex = 0;
		}
		else{
			this.visibleIndex++;
		}
		createAndShowGui();
	}
	/**
	 * revert() method is called in response to user pressing "hide"
	 *  alters visibleIndex- attribute denoting activeQueue 
	 * Index currently shown-sets to 0, creates new gui with updated list view
	 * @param ServerQueueModel: model
	 */
	public void revert(ServerQueueModel model){
		if(model.getSize() == 0){
			System.out.println("Queue is empty");
			return;
		}
		this.visibleIndex = 0;

		createAndShowGui();
	}
	
	/**
	 * viewPrev() method is called in response to user pressing "previous"
	 * method alters visibleIndex-attribute denoting activeQueue 
	 * Index currently shown- decreases by one, and creates
	 * new gui with updated list view
	 * @param ServerQueueModel: model
	 */
	public void viewPrev(ServerQueueModel model){
		if(model.getSize() == 0){
			System.out.println("Queue is empty");
			return;
		}
		if (this.visibleIndex == 0){
			this.visibleIndex = model.getSize()-1;
		}else{
			this.visibleIndex--;
		}
		createAndShowGui();
	}
	
	/**
	 * register() method registers queueview buttons with queuecontroller
	 * @param ServerQueueController : controller
	 */
	public void register(ServerQueueController controller){
		view.addActionListener(controller);
		skip.addActionListener(controller);
		prev.addActionListener(controller);
		revert.addActionListener(controller);
	}
	/**
	 * showOptions results in a confirmation question pop up after a 
	 * user selects a table to seat a guest. 
	 * @return int: 0 for yes, 1 for no
	 */
	public int showOptions(String s){
		
		return JOptionPane.showConfirmDialog(null,
	             "Are you sure you want to " + s + "?", "choose one", JOptionPane.YES_NO_OPTION);
	}
	/**
	 * unavailableError() results in a JDialog error pop up 
	 * when a user attempts to seat an already occupied table 
	 * @param String: n for table number
	 */
	public void unavailableError(String n){
		 JOptionPane.showMessageDialog(null, "Invalid table. " +n+ " is currently occupied." , "Error", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * WaitError() results in a JDialog error pop up for when all tables are taken
	 */
	public void WaitError(){
		 JOptionPane.showMessageDialog(null, "There are no available tables. Please refer to waitlist" , "Error", JOptionPane.ERROR_MESSAGE);
	}
}