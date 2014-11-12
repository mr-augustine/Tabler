package tabler.components.server;

import java.util.ArrayList;
import java.util.LinkedList;

public class ServerQueueModel {
	private LinkedList<ServerModel> activeQueue;
	private ArrayList<ServerModel> fullServers;
	
	public ServerQueueModel(ArrayList<ServerModel> servers) {
		// TODO fill in the constructor
	}

	/**
	 * This function returns a true or false value depending
	 * on if the server is next up
	 * Peak shows the head of the queue (least recent put in FIFO)
	 */
	public boolean isNext(){
		if (this.assignedSection.isFull()){
			return false;
		}
		else 
			return true;
	}
	public void updateQueue(){
		/**
		 * Adds any removed ServerModels back to the queue when sections are not full
		 */
		for(int i = 0; i < removedFromQueue.size(); i++){

			if (removedFromQueue.get(i).assignedSection.isFull() != true){
				int k = 0;
				while(addToQueue.get(k).assignedSection.TimeLastSeated() < removedFromQueue.get(i).assignedSection.TimeLastSeated()){
					k++;
				}
				addToQueue.add(k,removedFromQueue.get(i));
			}
		}
		/**
		 * Removes any servers with full sections from the available queue
		 */
		for (int i = 0; i < addToQueue.size(); i++){
			if(addToQueue.get(i).assignedSection.isFull() == true){
				removedFromQueue.add(addToQueue.get(i));
				addToQueue.remove(i);
				continue;
			}
		}
		/**
		 * Calculates the servermodel with the most recent assigned table time (needs date accommodation)
		 */
		ServerModel lastAssigned = addToQueue.get(0);
		int moveIndex = 0;
		for(int i = 1; i < addToQueue.size(); i++){
			if(addToQueue.get(i).assignedSection.TimeLastSeated() > lastAssigned.assignedSection.TimeLastSeated()){
				lastAssigned = addToQueue.get(i);
				moveIndex = i;
			}
		}
		update(addToQueue, lastAssigned, moveIndex);
		return;
		
	}
	/**
	 * Moves the most recent assigned servermodel to end of queue
	 * @param queue
	 * @param last
	 * @param moveIndex
	 */
	public void update(LinkedList<ServerModel> queue, ServerModel last, int moveIndex){
		queue.remove(moveIndex);
		queue.add(last);
		return;
	}
	/**
	 * This is a queue to keep track of Server's who are
	 * available in a Fist In First Out order
	 */
	public LinkedList<ServerModel> addToQueue = new LinkedList<ServerModel>();

	/**
	 * This is a queue to keep track of Server's who have been previously
	 * removed from the upcoming seat queue
	 */
	public LinkedList<ServerModel> removedFromQueue = new LinkedList<ServerModel>();
}
