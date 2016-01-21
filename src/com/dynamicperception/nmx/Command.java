package com.dynamicperception.nmx;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Michael Ploof
 *
 * @param <T> Return type of the command
 * @param <D> Data type to be sent with the command
 */
public class Command<T, D> {
	
	private final static float FLOAT_CONVERSION = 100f;
	private final static int MOTOR_COUNT = 3;
	private static int addr = 3;
	private static boolean debug = false;	
	private static List<Command<?, ?>> generalList = new ArrayList<Command<?, ?>>();	
	private static List<Command<?, ?>> motorList = new ArrayList<Command<?, ?>>();	
	private static List<Command<?, ?>> cameraList = new ArrayList<Command<?, ?>>();	
	private static List<Command<?, ?>> keyFrameList = new ArrayList<Command<?, ?>>();		
	private static boolean listsInitialized = false;
	
	private String name;	
	private Type type;	
	private int subaddr;
	private int command;
	private Class<?> returnType;
	private Class<?> dataType;
	private int dataLength;	
	private HelpCommand helpCommand;
	private PreCommand preCommand;	
	private PostCommand postCommand;
	
	public void help(){
		helpCommand.helpCommand();
	}
	
	public void setHelpCommand(HelpCommand helpCommand){
		this.helpCommand = helpCommand;
	}
	
	public static interface HelpCommand{
		public void helpCommand();
	}
	
	public static interface PreCommand{
		public String run(int subAddr, String dataStr);		
	}
	
	public void setPreCommand(PreCommand pre){
		this.preCommand = pre;
	}
	
	/**
	 * This interface has single method, run(). When a class object implementing this interface is set as
	 * a command object's postCommand (see {@link Command#setPostCommand(PostCommand)}),
	 * this method will be run after execution of the command. <br><br> This may be used to save
	 * the return value to an outside model, or to modify the return value in some way
	 * (for instance, converting from variable microsteps to fixed 16th microsteps). 
	 * See {@link PostCommand#run(int, String)} for more details. 
	 * @author Michael
	 *
	 */
	public interface PostCommand{
		/**
		 * This method allows for insertion of an operation immediately after a command's return
		 * value has been retrieved. The return value is passed to the run() method as a string for
		 * genericizing purposes. If you wish to convert the string to the command's normal return
		 * type while working with it, call the method {@link Command#parseReturnString(String)}.
		 * @param subaddr The command's sub-address
		 * @param returnVal The command's return value as a string
		 * @return
		 */
		public String run(int subaddr, String returnVal);
	}
	
	public void setPostCommand(PostCommand post){
		this.postCommand = post;
	}
	
	/**
	 * Reference values for setting debug state on NMX
	 * @author Michael
	 *
	 */
	public static class NMXDebug {
		public static final int COM = 1;
		public static final int STEPS = 2;
		public static final int MOTOR = 4;
		public static final int GEN = 8;
		public static final int FUNCTION = 16;
		public static final int CONFIRM = 32;
	}
	
	
	/**
	 * String names for each NMX motor command
	 * @author Michael
	 *
	 */
	public static class Names{	
 
		public static class General {
			/* General Commands */
			
			// Commands
			public static final String START_PROGRAM						= "g.start";
			public static final String PAUSE_PROGRAM						= "g.pause";			
			public static final String STOP_PROGRAM			 				= "g.stop	";
			public static final String TOGGLE_LED	 						= "g.toggleLed";
			public static final String SET_TIMING_MASTER 					= "g.setTimingMaster";
			public static final String SET_NAME 							= "g.setName";			
			public static final String SET_ADDRESS 							= "g.setAddress";
			public static final String SET_COMMON_LINE 	 					= "g.setCommonLine";
			public static final String SEND_ALL_MOTORS_HOME 				= "g.sendAllHome";
			public static final String SET_MAX_STEP_RATE					= "g.setMaxStepRate";
			
			public static final String SET_INPUT_EDGE						= "g.setInputEdge";
			public static final String SET_ALT_IO_MODE						= "g.setAltIOMode";
			public static final String SET_WATCHDOG							= "g.setWatchdog";
			
			public static final String SET_ALT_OUT_B4_SHOT_DELAY_TIME 		= "g.setAltOutBeforeShotDelayTime";
			public static final String SET_ALT_OUT_AFTER_SHOT_DELAY_TIME 	= "g.setAltOutAfterShotDelayTime";
			public static final String SET_ALT_OUT_B4_SHOT_TIME 			= "g.setAltOutBeforeShotTime";
			public static final String SET_ALT_OUT_AFTER_SHOT_TIME 			= "g.setAltOutAfterShotTime";			
			public static final String SET_ALT_OUT_TRIGGER_LEVEL			= "g.setAltOutTriggerLevel";
			
			public static final String SET_MAX_PROGRAM_TIME					= "g.setMaxProgramTime";
			public static final String SET_PROGRAM_MODE						= "g.setProgramMode"; 
			public static final String SET_JOYSTICK							= "g.setJoystick";
			public static final String SET_PINGPONG 						= "g.setPingpong";			
			public static final String SEND_ALL_MOTORS_START				= "g.sendAllStart";
			public static final String SET_START_HERE						= "g.setStartHere";
			public static final String SET_STOP_HERE						= "g.setStopHere";
			public static final String REVERSE_START_STOP					= "g.reverseStartStop";
			public static final String SET_FPS								= "g.setFPS";			
			public static final String SET_GRAFFIK							= "g.setGraffik";
			public static final String SET_APP								= "g.setApp";
			public static final String SET_PROGRAM_DELAY					= "g.setProgramDelay";			
			public static final String SET_DEBUG							= "g.setDebug";
						
			// Queries		
			public static final String GET_FIRMWARE							= "g.getFirmware";
			public static final String GET_RUN_STATUS 						= "g.getRunStatus";
			public static final String GET_RUN_TIME 						= "g.getRunTime";
			public static final String IS_EXPOSING							= "g.isExposing";
			public static final String IS_TIMING_MASTER 					= "g.isTimingMaster";
			public static final String GET_NAME								= "g.getName";
			public static final String GET_MAX_STEP_RATE 					= "g.getMaxStepRate";
			public static final String GET_VOLTAGE 							= "g.getVoltage";
			public static final String GET_CURRENT 							= "g.getCurrent";
			public static final String GET_ALT_INPUT_EDGE					= "g.getAltInputEdge";
			public static final String GET_ALT_IO_MODE						= "g.getAltIOMode";			
			public static final String GET_LIMIT_SWITCH_STATE				= "g.getLimitSwitchState";
			public static final String GET_ALT_OUT_B4_SHOT_DELAY_TIME 		= "g.getAltOutBeforeShotDelayTime";
			public static final String GET_ALT_OUT_AFTER_SHOT_DELAY_TIME 	= "g.getAltOutAfterShotDelayTime";
			public static final String GET_ALT_OUT_B4_SHOT_TIME 			= "g.getAltOutBeforeShotTime";
			public static final String GET_ALT_OUT_AFTER_SHOT_TIME 			= "g.getAltOutAfterShotTime";			
			public static final String GET_ALT_OUT_TRIGGER_LEVEL			= "g.getAltOutTriggerLevel";
			public static final String GET_PROGRAM_DELAY					= "g.getProgramDelay";
			public static final String GET_PROGRAM_MODE 					= "g.getProgramMode";
			public static final String GET_POWER_CYCLED						= "g.getPowerCyled";	
			public static final String GET_JOYSTICK							= "g.getJoystick";	
			public static final String GET_PINGPONG							= "g.getPingpong";
			public static final String GET_WATCHDOG							= "g.getWatchdog";
			public static final String GET_PCT_COMP							= "g.getPctComplete";
			public static final String GET_MOT_ATTCH						= "g.getMotorAttach";
			public static final String GET_TOTAL_RUN_TIME					= "g.getTotalRunTime";
			public static final String IS_PROGRAM_COMPLETE					= "g.isProgramComplete";
			public static final String GET_FPS 								= "g.getFPS";			
			public static final String GET_MOTOR_RUNNING_STATES				= "g.getMotorRunningStates";
			public static final String IS_PROGRAM_VALID						= "g.isProgramValid";
			public static final String GET_MOTOR_SLEEP_STATES				= "g.getMotorSleepStates";
			public static final String IS_GRAFFIK							= "g.isGraffik";	
			
			public static final String GET_FREE_MEMORY	 					= "g.getFreeMemory";				
		}
		
		public static class Motor {
			
			/* Motor Commands */
				
			//Commands
			public static final String SET_SLEEP 		= "m.setSleep";
			public static final String SET_ENABLE 		= "m.setEnable";			
			public static final String SET_BACKLASH 	= "m.setBacklash";
			public static final String SET_MICROSTEPS 	= "m.setMS";			
			
			public static final String RESET_LIMITS 	= "m.resetLimits";
			public static final String SET_HOME 		= "m.setHome";		
			public static final String SET_END_HERE 	= "m.setEndHere";
			
			public static final String SET_START_HERE	= "m.setStartHere";
			public static final String SET_STOP_HERE 	= "m.setStopHere";
			public static final String SET_START		= "m.setStart";
			public static final String SET_STOP			= "m.setStop";			
			public static final String SET_EASING		= "m.setEasing";
			public static final String SET_LEAD_IN		= "m.setLeadIn";
			public static final String SET_TRAVEL		= "m.setTravel";
			public static final String SET_PROG_ACCEL	= "m.setProgramAccel";
			public static final String SET_PROG_DECEL	= "m.setProgramDecel";
			public static final String SET_LEAD_OUT		= "m.setLeadOut";
			public static final String AUTO_SET_MS		= "m.autoSetMS";
			
			public static final String SET_POS 			= "m.setPos";
			
			public static final String SEND_HOME 		= "m.sendHome";
			public static final String SEND_END 		= "m.sendEnd";
			public static final String SEND_START 		= "m.sendStart";
			public static final String SEND_STOP		= "m.sendStop";
			public static final String SEND_TO 			= "m.sendTo";
			
			public static final String STOP_MOTOR 		= "m.stop";
			public static final String SET_MAX_SPEED 	= "m.setMaxSpeed";
			public static final String SET_DIR 			= "m.setDir";
			public static final String SET_SPEED		= "m.setSpeed";
			public static final String SET_ACCEL		= "m.setAccel";						
			
			// Queries
			public static final String IS_RUNNING 		= "m.isRunning";
			public static final String GET_ENABLE 		= "m.isEnabled";			
			public static final String GET_BACKLASH 	= "m.getBacklash";
			public static final String GET_MS 			= "m.getMS";
			public static final String GET_END 			= "m.getEnd";
			public static final String GET_POS 			= "m.getPos";			
			public static final String GET_SPEED 		= "m.getSpeed";
			public static final String GET_ACCEL 		= "m.getAccel";
			public static final String GET_START 		= "m.getStart";
			public static final String GET_STOP 		= "m.getStop";
			public static final String GET_TRAVEL		= "m.getTravel";
			public static final String GET_SLEEP 		= "m.getSleep";
			public static final String GET_DIR 			= "m.getDir";
			public static final String GET_MAX_STEP_RATE = "m.getMaxStepRate";		
			public static final String GET_EASING		= "m.getEasing";
			public static final String GET_LEAD_IN		= "m.getLeadIn";
			public static final String GET_PROG_ACCEL	= "m.getProgramAccel";
			public static final String GET_PROG_DECEL	= "m.getProgramDecel";
			public static final String GET_LEAD_OUT		= "m.getLeadOut";
			public static final String IS_SPEED_VALID	= "m.isSpeedValid";

		}
		
		public static class Camera {
			/* Camera Commands */
			
			//Commands
			public static final String SET_ENABLE 				= "c.setEnable";			
			public static final String EXPOSE_NOW 				= "c.expose";
			public static final String SET_TRIGGER 				= "c.setTrigger";
			public static final String SET_FOCUS 				= "c.setFocus";
			public static final String SET_MAX_SHOTS			= "c.setMaxShots";
			public static final String SET_DELAY	 			= "c.setDelay";
			public static final String SET_FOCUS_WITH_SHUTTER	= "c.setFocusWithShutter";
			public static final String SET_INTERVAL				= "c.setInterval";		
			public static final String SET_MUP		 			= "c.setMUP";
			public static final String SET_TEST_MODE 			= "c.setTestMode";
			public static final String SET_KEEPALIVE			= "c.setKeepAlive";
			
			// Queries
			public static final String IS_ENABLED				= "c.isEnabled";
			public static final String IS_EXPOSING				= "c.isExposing";
			public static final String GET_TRIGGER				= "c.getTrigger";
			public static final String GET_FOCUS 				= "c.getFocus";
			public static final String GET_MAX_SHOTS 			= "c.getMaxShots";
			public static final String GET_DELAY	 			= "c.getDelay";	
			public static final String GET_FOCUS_WITH_SHUTTER	= "c.getFocusWithShutter";
			public static final String GET_MUP 					= "c.getMUP";
			public static final String GET_INTERVAL				= "c.getInterval";			
			public static final String GET_SHOTS				= "c.getShots";	
			public static final String GET_TEST_MODE			= "c.getTestMode";
			public static final String GET_KEEPALIVE			= "c.getKeepAlive";

		}
		
		public static class KeyFrame {
			// Commands
			public static final String SET_AXIS 		= "k.setAxis";
			public static final String SET_COUNT		= "k.setCount";			
			public static final String SET_ABSCISSA 	= "k.setAbscissa";
			public static final String SET_POS			= "k.setPos";
			public static final String SET_VEL			= "k.setVel";		
			
			public static final String SET_UPDATE_RATE 		= "k.setUpdateRate";
			public static final String END_TRANSMISSION		= "k.endTransmission";		
			public static final String SET_CONT_VID_TIME	= "k.setContVidTime";
			
			public static final String START_PROGRAM	= "k.startProgram";
			public static final String PAUSE_PROGRAM 	= "k.pauseProgram";
			public static final String STOP_PROGRAM		= "k.stopProgram";
			
			// Queries
			public static final String PRINT_INFO		= "k.printInfo";			
			public static final String GET_COUNT 		= "k.getCount";			
			public static final String GET_UPDATE_RATE 	= "k.getUpdateRate";
			public static final String GET_POS_AT		= "k.getPosAt";
			public static final String GET_VEL_AT 		= "k.getVelAt";
			public static final String GET_ACCEL_AT		= "k.getAccelAt";			
			public static final String IS_VEL_VALID		= "k.isVelValid";
			public static final String IS_ACCEL_VALID	= "k.isAccelValid";
			public static final String GET_RUN_STATE 	= "k.getRunState";
			public static final String GET_RUN_TIME		= "k.getRunTime";
			public static final String GET_MAX_RUN_TIME = "k.getMaxRunTime";
			public static final String GET_PCT_DONE		= "k.getPctDone";			
			
			public static final String GET_KF_ABSCISSA	= "k.getKfTime";
			public static final String GET_KF_POS		= "k.getKfPos";
			public static final String GET_KF_VEL		= "k.getKfVel";
		}
	}
	
	/**
	 * Possible command types: GENERAL, MOTOR, CAMERA, KEYFRAME, NOT_A_TYPE
	 * @author Michael
	 *
	 */
	public static enum Type{
		GENERAL, MOTOR, CAMERA, KEYFRAME, NOT_A_TYPE;
	}
	
	/* Constructor and Initialization Method */
		
	/** Private constructor
	 * @param type Command type, see {@link Type}
	 * @param command Command number; must correspond to switch case number in NMX firmware.
	 * @param name	Name of the command as a String
	 */
	private Command(Command.Type type, int command, String name){		
		this.init(type, command, name);
	}	
	
	@SuppressWarnings("null")
	private void init(Command.Type type, int command, String name){
		T returnTypeDummy = null;
		D dataTypeDummy = null;		
		this.dataType = dataTypeDummy.getClass();
		this.returnType = returnTypeDummy.getClass();
		this.helpCommand = null;
		this.preCommand = null;
		this.postCommand = null;
		this.name = name;
		this.type = type;
		this.command = command;			
		if(type == Command.Type.GENERAL){
			this.subaddr = 0;
		}	
		else if(type == Command.Type.MOTOR){
			this.subaddr = 1;
		}
		else if(type == Command.Type.CAMERA){
			this.subaddr = 4;			
		}
		else if(type == Command.Type.KEYFRAME){
			this.subaddr = 5;
		}		
		this.dataLength = dataType == Byte.class ? 1 : dataType == Integer.class ? 2 : dataType == Long.class || dataType == Float.class ? 4 : 0;
		class DefaultHelp implements HelpCommand{
			Command<?, ?> command;			
			public DefaultHelp(Command<?, ?> command){
				this.command = command;
			}
			@Override
			public void helpCommand() {
				command.printInfo();
				System.out.println("No additional help available for this command");				
			}			
		}
		this.helpCommand = new DefaultHelp(this);
	}
/* Static Methods */	
	/** 
	 * @param subAddr Sub-address number
	 * @param command Command number
	 * @return Command name as String object
	 */
	public static String getCommandName(int subAddr, int command){
		Command.checkInitialization();
		if(subAddr >= 1 && subAddr <= 3)
			subAddr = 1;
		
		switch(subAddr){
		case 0:
			for(Command<?, ?> thisCommand : generalList){
				if(thisCommand.getCommandNum() == command){
					return thisCommand.getName();
				}
			}
			break;
		case 1:
			for(Command<?, ?> thisCommand : motorList){
				if(thisCommand.getCommandNum() == command){
					return thisCommand.getName();
				}
			}
			break;
		case 4:
			for(Command<?, ?> thisCommand : cameraList){
				if(thisCommand.getCommandNum() == command){
					return thisCommand.getName();
				}
			}
			break;
		case 5:
			for(Command<?, ?> thisCommand : keyFrameList){
				if(thisCommand.getCommandNum() == command){
					return thisCommand.getName();
				}
			}
			break;		
		}	
		return "No such command";		
	}
	
	/**
	 * Checks whether the command lists have been initialized
	 * and initializes them if they have not.
	 */
	public static void checkInitialization(){
		if(listsInitialized)
			return;
		
		initCommands();
		listsInitialized = true;
	}
	
	/**
	 * Populates the command lists
	 */
	private static void initCommands(){
		
		//******** Help Commands ********//
		class DebugHelp implements HelpCommand{
			@Override
			public void helpCommand() {				
				System.out.println("\nSyntax: g.setDebug <DEBUG_CODE>");
				System.out.println("Valid codes:");
				System.out.println("1 - Raw command trace");			
				System.out.println("2 - Motor steps");
				System.out.println("4 - General motor info");
				System.out.println("8 - Serial command detail (descriptive output)");
				System.out.println("16 - Function detail");
				System.out.println("32 - Command success / failure message");				
			}			
		}
		
		//******** General Commands ********//
		
		// Create the general commands		
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 2, Names.General.START_PROGRAM));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 3, Names.General.PAUSE_PROGRAM));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 4, Names.General.STOP_PROGRAM));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 5, Names.General.TOGGLE_LED));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 6, Names.General.SET_TIMING_MASTER));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 7, Names.General.SET_NAME));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 8, Names.General.SET_ADDRESS));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 9, Names.General.SET_COMMON_LINE));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 10, Names.General.SEND_ALL_MOTORS_HOME));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 11, Names.General.SET_MAX_STEP_RATE));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 12, Names.General.SET_INPUT_EDGE));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 13, Names.General.SET_ALT_IO_MODE));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 14, Names.General.SET_WATCHDOG));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 15, Names.General.SET_ALT_OUT_B4_SHOT_DELAY_TIME));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 16, Names.General.SET_ALT_OUT_AFTER_SHOT_DELAY_TIME));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 17, Names.General.SET_ALT_OUT_B4_SHOT_TIME));
		generalList.add(new Command<Void, Integer>(Command.Type.GENERAL, 18, Names.General.SET_ALT_OUT_AFTER_SHOT_TIME));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 19, Names.General.SET_ALT_OUT_TRIGGER_LEVEL));
		generalList.add(new Command<Void, Long>(Command.Type.GENERAL, 20, Names.General.SET_MAX_PROGRAM_TIME));
		generalList.add(new Command<Void, Long>(Command.Type.GENERAL, 21, Names.General.SET_PROGRAM_DELAY));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 22, Names.General.SET_PROGRAM_MODE));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 23, Names.General.SET_JOYSTICK));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 24, Names.General.SET_PINGPONG));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 25, Names.General.SEND_ALL_MOTORS_START));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 26, Names.General.SET_START_HERE));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 27, Names.General.SET_STOP_HERE));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 28, Names.General.SET_FPS));
		generalList.add(new Command<Void, Void>(Command.Type.GENERAL, 29, Names.General.REVERSE_START_STOP));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 50, Names.General.SET_GRAFFIK));
		generalList.add(new Command<Void, Byte>(Command.Type.GENERAL, 51, Names.General.SET_APP));
		generalList.add(new Command<Void, Long>(Command.Type.GENERAL, 52, Names.General.SET_PROGRAM_DELAY));
		
		// Queries
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 100, Names.General.GET_FIRMWARE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 101, Names.General.GET_RUN_STATUS));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 102, Names.General.GET_RUN_TIME));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 103, Names.General.IS_EXPOSING));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 104, Names.General.IS_TIMING_MASTER));
		generalList.add(new Command<String, Void>(Command.Type.GENERAL, 105, Names.General.GET_NAME));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 106, Names.General.GET_MAX_STEP_RATE));
		generalList.add(new Command<Float, Void>(Command.Type.GENERAL, 107, Names.General.GET_VOLTAGE));
		generalList.add(new Command<Float, Void>(Command.Type.GENERAL, 108, Names.General.GET_CURRENT));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 109, Names.General.GET_ALT_INPUT_EDGE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 110, Names.General.GET_ALT_IO_MODE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 111, Names.General.GET_LIMIT_SWITCH_STATE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 112, Names.General.GET_ALT_OUT_B4_SHOT_DELAY_TIME));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 113, Names.General.GET_ALT_OUT_AFTER_SHOT_DELAY_TIME));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 114, Names.General.GET_ALT_OUT_B4_SHOT_TIME));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 115, Names.General.GET_ALT_OUT_AFTER_SHOT_TIME));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 116, Names.General.GET_ALT_OUT_TRIGGER_LEVEL));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 117, Names.General.GET_PROGRAM_DELAY));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 118, Names.General.GET_PROGRAM_MODE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 119, Names.General.GET_POWER_CYCLED));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 120, Names.General.GET_JOYSTICK));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 121, Names.General.GET_PINGPONG));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 122, Names.General.GET_WATCHDOG));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 123, Names.General.GET_PCT_COMP));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 124, Names.General.GET_MOT_ATTCH));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 125, Names.General.GET_TOTAL_RUN_TIME));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 126, Names.General.IS_PROGRAM_COMPLETE));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 127, Names.General.GET_FPS));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 128, Names.General.GET_MOTOR_RUNNING_STATES));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 129, Names.General.IS_PROGRAM_VALID));
		generalList.add(new Command<Integer, Void>(Command.Type.GENERAL, 130, Names.General.GET_MOTOR_SLEEP_STATES));
		generalList.add(new Command<Boolean, Void>(Command.Type.GENERAL, 150, Names.General.IS_GRAFFIK));
		
		Command<Void, Byte> setDebug = new Command<Void, Byte>(Command.Type.GENERAL, 254, Names.General.SET_DEBUG);		
		setDebug.setHelpCommand(new DebugHelp());
		generalList.add(setDebug);
	
		
		//******** Motor Commands ********//
		
		// Create the general commands		
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 2, Names.Motor.SET_SLEEP));
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 3, Names.Motor.SET_ENABLE));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 4, Names.Motor.STOP_MOTOR));
		motorList.add(new Command<Void, Integer>(Command.Type.MOTOR, 5, Names.Motor.SET_BACKLASH));
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 6, Names.Motor.SET_MICROSTEPS));
		motorList.add(new Command<Void, Integer>(Command.Type.MOTOR, 7, Names.Motor.SET_MAX_SPEED));
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 8, Names.Motor.SET_DIR));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 9, Names.Motor.SET_HOME));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 10, Names.Motor.SET_END_HERE));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 11, Names.Motor.SEND_HOME));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 12, Names.Motor.SEND_END));
		motorList.add(new Command<Void, Float>(Command.Type.MOTOR, 13, Names.Motor.SET_SPEED));
		motorList.add(new Command<Void, Float>(Command.Type.MOTOR, 14, Names.Motor.SET_ACCEL));
		
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 16, Names.Motor.SET_START));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 17, Names.Motor.SET_STOP));
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 18, Names.Motor.SET_EASING));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 19, Names.Motor.SET_LEAD_IN));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 20, Names.Motor.SET_TRAVEL));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 21, Names.Motor.SET_PROG_ACCEL));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 22, Names.Motor.SET_PROG_DECEL));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 23, Names.Motor.SEND_START));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 24, Names.Motor.SEND_STOP));
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 25, Names.Motor.SET_LEAD_OUT));
		
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 27, Names.Motor.RESET_LIMITS));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 28, Names.Motor.AUTO_SET_MS));
		motorList.add(new Command<Void, Void>(Command.Type.MOTOR, 29, Names.Motor.SET_START_HERE));
		motorList.add(new Command<Void, Byte>(Command.Type.MOTOR, 30, Names.Motor.SET_STOP_HERE));	
		motorList.add(new Command<Void, Long>(Command.Type.MOTOR, 31, Names.Motor.SEND_TO));
				
		
		// Queries
		motorList.add(new Command<Boolean, Void>(Command.Type.MOTOR, 100, Names.Motor.GET_ENABLE));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 101, Names.Motor.GET_BACKLASH));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 102, Names.Motor.GET_MS));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 103, Names.Motor.GET_DIR));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 104, Names.Motor.GET_MAX_STEP_RATE));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 105, Names.Motor.GET_END));	
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 106, Names.Motor.GET_POS));		
		motorList.add(new Command<Boolean, Void>(Command.Type.MOTOR, 107, Names.Motor.IS_RUNNING));
		motorList.add(new Command<Float, Void>(Command.Type.MOTOR, 108, Names.Motor.GET_SPEED));
		motorList.add(new Command<Float, Void>(Command.Type.MOTOR, 109, Names.Motor.GET_ACCEL));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 110, Names.Motor.GET_EASING));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 111, Names.Motor.GET_START));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 112, Names.Motor.GET_STOP));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 113, Names.Motor.GET_TRAVEL));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 114, Names.Motor.GET_LEAD_IN));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 115, Names.Motor.GET_PROG_ACCEL));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 116, Names.Motor.GET_PROG_DECEL));
		motorList.add(new Command<Boolean, Void>(Command.Type.MOTOR, 117, Names.Motor.GET_SLEEP));
		motorList.add(new Command<Boolean, Void>(Command.Type.MOTOR, 118, Names.Motor.IS_SPEED_VALID));
		motorList.add(new Command<Integer, Void>(Command.Type.MOTOR, 119, Names.Motor.GET_LEAD_OUT));
	
		
		//******** Camera Commands ********//
		
		// Commands		
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 2, Names.Camera.SET_ENABLE));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 3, Names.Camera.EXPOSE_NOW));
		cameraList.add(new Command<Void, Long>(Command.Type.CAMERA, 4, Names.Camera.SET_TRIGGER));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 5, Names.Camera.SET_FOCUS));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 6, Names.Camera.SET_MAX_SHOTS));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 7, Names.Camera.SET_DELAY));
		cameraList.add(new Command<Void, Void>(Command.Type.CAMERA, 8, Names.Camera.SET_FOCUS_WITH_SHUTTER));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 9, Names.Camera.SET_MUP));
		cameraList.add(new Command<Void, Long>(Command.Type.CAMERA, 10, Names.Camera.SET_INTERVAL));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 11, Names.Camera.SET_TEST_MODE));
		cameraList.add(new Command<Void, Integer>(Command.Type.CAMERA, 12, Names.Camera.SET_KEEPALIVE));
		
		// Queries		
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 100, Names.Camera.IS_ENABLED));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 101, Names.Camera.IS_EXPOSING));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 102, Names.Camera.GET_TRIGGER));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 103, Names.Camera.GET_FOCUS));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 104, Names.Camera.GET_MAX_SHOTS));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 105, Names.Camera.GET_DELAY));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 106, Names.Camera.GET_FOCUS_WITH_SHUTTER));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 107, Names.Camera.GET_MUP));		
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 108, Names.Camera.GET_INTERVAL));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 109, Names.Camera.GET_SHOTS));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 110, Names.Camera.GET_TEST_MODE));
		cameraList.add(new Command<Integer, Void>(Command.Type.CAMERA, 111, Names.Camera.GET_KEEPALIVE));
	
		
		//******** Key Frame Commands ********//
		
		// Commands		
		keyFrameList.add(new Command<Integer, Integer>(Command.Type.KEYFRAME, 10, Names.KeyFrame.SET_AXIS));		
		keyFrameList.add(new Command<Integer, Integer>(Command.Type.KEYFRAME, 11, Names.KeyFrame.SET_COUNT));
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 12, Names.KeyFrame.SET_ABSCISSA));
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 13, Names.KeyFrame.SET_POS));		
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 14, Names.KeyFrame.SET_VEL));
		keyFrameList.add(new Command<Integer, Integer>(Command.Type.KEYFRAME, 15, Names.KeyFrame.SET_UPDATE_RATE));
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 16, Names.KeyFrame.END_TRANSMISSION));
		keyFrameList.add(new Command<Integer, Long>(Command.Type.KEYFRAME, 17, Names.KeyFrame.SET_CONT_VID_TIME));
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 20, Names.KeyFrame.START_PROGRAM));
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 21, Names.KeyFrame.PAUSE_PROGRAM));
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 22, Names.KeyFrame.STOP_PROGRAM));
		
		// Queries		
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 99, Names.KeyFrame.PRINT_INFO));
		keyFrameList.add(new Command<Integer, Void>(Command.Type.KEYFRAME, 100,Names.KeyFrame.GET_COUNT));
		keyFrameList.add(new Command<Integer, Void>(Command.Type.KEYFRAME, 101,Names.KeyFrame.GET_UPDATE_RATE));
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 102, Names.KeyFrame.GET_POS_AT));		
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 103, Names.KeyFrame.GET_VEL_AT));
		keyFrameList.add(new Command<Float, Float>(Command.Type.KEYFRAME, 104, Names.KeyFrame.GET_ACCEL_AT));
		keyFrameList.add(new Command<Void, Void>(Command.Type.KEYFRAME, 105, Names.KeyFrame.IS_VEL_VALID));
		keyFrameList.add(new Command<Boolean, Void>(Command.Type.KEYFRAME, 106, Names.KeyFrame.IS_ACCEL_VALID));		

		keyFrameList.add(new Command<Integer, Void>(Command.Type.KEYFRAME, 120, Names.KeyFrame.GET_RUN_STATE));
		keyFrameList.add(new Command<Long, Void>(Command.Type.KEYFRAME, 121, Names.KeyFrame.GET_RUN_TIME));
		keyFrameList.add(new Command<Long, Void>(Command.Type.KEYFRAME, 122, Names.KeyFrame.GET_MAX_RUN_TIME));
		keyFrameList.add(new Command<Integer, Void>(Command.Type.KEYFRAME, 123, Names.KeyFrame.GET_PCT_DONE));
		keyFrameList.add(new Command<Integer, Integer>(Command.Type.KEYFRAME, 130, Names.KeyFrame.GET_KF_ABSCISSA));
		keyFrameList.add(new Command<Integer, Integer>(Command.Type.KEYFRAME, 131, Names.KeyFrame.GET_KF_POS));
		keyFrameList.add(new Command<Float, Integer>(Command.Type.KEYFRAME, 132, Names.KeyFrame.GET_KF_VEL));
	}
	
	/** 
	 * Fetch command list from type
	 * @param type The type of commands wanted
	 * @return The list of command objects associated with that type
	 */
	private static List<Command<?, ?>> getList(Type type){
		
		if(type == Type.GENERAL){
			return generalList;
		}
		else if(type == Type.MOTOR){
			return motorList;
		}
		else if(type == Type.CAMERA){
			return cameraList;
		}
		else if(type == Type.KEYFRAME){
			return keyFrameList;
		}
		else{
			System.out.println("That is not a supported command type");
			throw new UnsupportedOperationException();
		}
	}	
	
	/**
	 * @param name Name of the command including type prefix (e.g. "g.setDebug")
	 * @return The command's Type enum
	 */
	public static Type getType(String name){		
		if(name.substring(0,1).equals("g")){
			return Type.GENERAL;
		}
		else if(name.substring(0,1).equals("m")){			
			return Type.MOTOR;
		}
		else if(name.substring(0,1).equals("c")){
			return Type.CAMERA;
		}
		else if(name.substring(0,1).equals("k")){
			return Type.KEYFRAME;
		}
		else{
			return Type.NOT_A_TYPE;
		}
	}
	
	/**
	 * Prints to the console a list of all commands for the specified type
	 * @param type The command type. See {@link Type}.
	 */
	public static void printList(Type type){				
		if(type == Type.NOT_A_TYPE){
			System.out.println("Not a valid command type");
			return;
		}
		Command.checkInitialization();
		List<Command<?, ?>> thisList = Command.getList(type);
		System.out.println("\n******** " + type + " COMMAND LIST ********");
		for(int i = 0; i < thisList.size(); i++){
			System.out.println(thisList.get(i).getName());
		}
	}
	
	/**
	 * Prints all commands of the specified type containing the search term
	 * @param term String with the following syntax: &ltTYPE&gt.&ltTERM&gt <br>
	 * For example: "g.get" would print all general commands with names containing "get"  
	 */
	public static void find(String term){
		term = term.toLowerCase();
		
		Command.checkInitialization();
		Type type = Command.getType(term);		
		if(type == Type.NOT_A_TYPE){
			System.out.println("Not a valid type");
			System.out.println("Preface search term with <TYPE>.");
			System.out.println("Valid types: g, m, c, k");
			return;
		}
		List<Command<?, ?>> thisList = Command.getList(type);
		System.out.println("\n******** Matching commands ********");
		// Trim the type indicator
		term = term.substring(2, term.length());
		for(int i = 0; i < thisList.size(); i++){
			Command<?, ?> thisCommand = thisList.get(i);
			if(thisCommand.getName().toLowerCase().indexOf(term) >= 0){
				System.out.println(thisCommand.getName());
			}
		}
	}
	
	/**
	 * Fetch command using name
	 * @param type
	 * @param name
	 * @return
	 */
	public static Command<?, ?> get(String name) throws UnsupportedOperationException{
		Command.checkInitialization();
		Type type = Command.getType(name);		
		List<Command<?, ?>> commandList = getList(type);
		for(int i = 0; i < commandList.size(); i++){
			Command<?, ?> thisCommand = commandList.get(i); 
			if(type == thisCommand.getType() && name.equals(thisCommand.getName())){
				return thisCommand;
			}
		}
		// If no command was found, it must have been invalid
		throw new UnsupportedOperationException();		
	}
	
	/**
	 * Fetch command using command number
	 * @param type
	 * @param command
	 * @return
	 */
	public static Command<?, ?> get(Type type, int command){		
		Command.checkInitialization();
		List<Command<?, ?>> commandList = getList(type);
		for(int i = 0; i < commandList.size(); i++){
			Command<?, ?> thisCommand = commandList.get(i); 
			if(type == thisCommand.getType() && command == thisCommand.getCommandNum()){
				return thisCommand;
			}
		}
		// If no command was thrown, it must have been invalid
		throw new UnsupportedOperationException();		
	}
	
	/** 
	 * @param debug Boolean, whether the Command class should print command details
	 * to the console when a command is executed 
	 */
	public static void setCommandDebug(boolean debug){
		Command.debug = debug;
	}
	
	/**
	 * @param debug Boolean: whether the NMXComs class should print serial details
	 * to the console when a command is executed
	 */
	public static void setNMXComsDebug(boolean debug){
		NMXComs.setSerialDetail(debug);
	}
	
	/** 
	 * Sets controller address to which commands will be sent. The default
	 * address is 3.
	 * @param addr Integer address
	 */
	public static void setAddr(int addr){
		Command.addr = addr;
	}
	
	/**
	 * Gets controller address to which commands will be sent
	 * @return Integer address
	 */
	public static int getAddr(){
		return Command.addr;
	}
	
	/* Non-Static Methods */	
	/**
	 * Returns the name of the command on which it is called 
	 * @return Command name as a string
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the type of the command on which it is called
	 * @return The 
	 */
	public Type getType(){
		return this.type;
	}
	
	public int getCommandNum(){
		return this.command;
	}
	
	public void printInfo(){
		System.out.println("Command type: " + this.type);
		System.out.println("Number: " + this.command);
		System.out.println("Name: " + this.name);
		System.out.println("Return type: " + this.returnType.getName());		
	}
	
	/**
	 * If this is not a motor command, throw an exception
	 */
	private void checkMotorCommand(){
		if(this.type != Type.MOTOR){
			System.out.println(this.getName() + " is not a motor command, so no motor parameter should be specified");
			throw new UnsupportedOperationException();
		}
	};
	
	/**
	 * If this is a motor command and requires a data parameter, throw an exception
	 */
	private void checkMissingData(){
		if(this.type == Type.MOTOR && this.dataType != Void.class){
			System.out.println(this.getName() + " command requires motor parameter and a data parameter");
			throw new UnsupportedOperationException();
		}
	};
		
	public T execute(boolean dataOrMotor){		
		checkMissingData();
		return this.execute(dataOrMotor == true ? "1" : "0");	
	}
	
	public T execute(int motor, boolean data){
		checkMotorCommand();
		return this.execute(Integer.toString(motor), data == true ? "1" : "0");
	}
	
	public T execute(int dataOrMotor){
		checkMissingData();	
		return this.execute(Integer.toString(dataOrMotor));
	}
	
	public T execute(int motor, int data){
		checkMotorCommand();
		return this.execute(Integer.toString(motor), Integer.toString(data));
	}
	
	public T execute(float dataOrMotor){
		checkMissingData();
		return this.execute(Float.toString(dataOrMotor));
	}
	
	public T execute(int motor, float data){
		checkMotorCommand();
		return this.execute(Integer.toString(motor), Float.toString(data));
	}
	
	public T execute(){
		if(this.type == Command.Type.MOTOR){
			System.out.println(this.getName() + " is a motor command; the motor number must be specified to execute");			
			throw new UnsupportedOperationException();
		}
		else{
			return execute(this.subaddr, "0", false);			
		}
	}
	
	private T execute(String dataOrMotor){
		if(this.type == Command.Type.MOTOR){
			int motor = Integer.parseInt(dataOrMotor);
			if(motor < 0 || motor > MOTOR_COUNT){
				System.out.println("Invalid motor number");
				throw new UnsupportedOperationException();				
			}
			int tempSubaddr = motor + 1;
			return execute(tempSubaddr, "0", false);
		}
		else{
			return execute(this.subaddr, dataOrMotor, true);
		}
	}
	
	private T execute(String motor, String data){
		if(this.type == Command.Type.MOTOR){
			int motorNum = Integer.parseInt(motor);
			if(motorNum < 0 || motorNum > MOTOR_COUNT){
				System.out.println("Invalid motor number");
				throw new UnsupportedOperationException();				
			}
			int tempSubaddr = motorNum + 1;
			return execute(tempSubaddr, data, true);
		}	
		else{
			System.out.println("This is a non-motor command; a motor number may not be specified");			
			throw new UnsupportedOperationException();				
		} 
	}
		
	@SuppressWarnings("unchecked")
	private T execute(int subAddr, String dataStr, boolean hasData){

		// Notify if data is attached to a command that does not take additional data
		if(dataLength == 0 && hasData){			
			System.out.println("This command does not send additional data");			
			throw new UnsupportedOperationException();
		}
		
		// Do any pre-command action
		if(preCommand != null)
			preCommand.run(subAddr, dataStr);
		
		// Parse the data, if necessary
		int data = 0;		
		if(hasData){			
			
			if(dataStr.equals("rand")){
				float speed = (float)(Math.random() * 10000) - 5000;
				data = Float.floatToIntBits(speed);
				System.out.println("Setting speed to: " + speed + " steps/s");
			}
			else if(dataType == Float.class){
				data = Float.floatToIntBits(Float.parseFloat(dataStr));
				
				System.out.println("Parsed float: " + data);
			}
			else{
				data = (int) Math.round(Float.parseFloat(dataStr));
				System.out.println("Parsed int: " + data);
			}
		}		
	
		// Send the command to the NMX
		if(hasData){			
			NMXComs.cmd(addr, subaddr, command, dataLength, data);
		}
		else{
			NMXComs.cmd(addr, subaddr, command);			
		}
		
		// Wait for the NMX to clear
		waitForNMX();	
		
		// Cast the return value to the proper response type
		T ret = null;		
		if(returnType == Integer.class){
			ret = (T) returnType.cast(NMXComs.getResponseVal());			
		}
		else if(returnType == Float.class){			
			ret = (T) returnType.cast((float) NMXComs.getResponseVal() / FLOAT_CONVERSION);
		}
		else if(returnType == Boolean.class){
			ret = (T) returnType.cast(NMXComs.getResponseVal() == 0 ? false : true);			
		}
		// Void return type
		else{			
			ret = (T) Void.class.cast(ret);
		}
		
		// Print debug if necessary
		if(debug){
			System.out.println("Command: " + this.name);
		}
		if(ret != null)
			System.out.println(ret);
		else
			System.out.println("OK!");
		
		// Do any post command action
		if(postCommand != null){		
			ret = parseReturnString(postCommand.run(subAddr, ret.toString()));			
		}		
		
		// Return the value
		return ret;
	}
	
	/**
	 * Converts a string representation of the command return value back to proper command return type.
	 * This may be useful when constructing postCommand() method implementations, since the method takes
	 * the return value as String as a parameter in order to facilitate generic use.
	 * @param retString The return value as a string
	 * @return The return value as a return class object
	 */
	@SuppressWarnings("unchecked")
	public T parseReturnString(String retString){
		if(returnType == Integer.class){
			return (T) returnType.cast(Integer.parseInt(retString));			
		}
		else if(returnType == Float.class){			
			return (T) returnType.cast(Float.parseFloat(retString));		
		}
		else if(returnType == Boolean.class){
			return (T) returnType.cast(Boolean.parseBoolean(retString));					
		}
		else if(returnType == String.class){
			return (T) retString;					
		}
		else{
			throw new UnsupportedOperationException();
		}
	}
	
	private static void waitForNMX(){
		// If a command is currently being sent, wait
		while(NMXComs.isSendingCommand()){	
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("Interrupted while sending command to NMX");
				e.printStackTrace();
			}
		}		
	}	

	/**
	 * Causes the thread to sleep for 10 milliseconds
	 */
	public static void commandWait(){
		commandWait(10);
	}
	
	/**
	 * Causes the thread to sleep for the specified time
	 * @param time The sleep time in milliseconds
	 */
	public static void commandWait(int time){
		while(NMXComs.isBusy()){			
			// Wait till the NMX communications class is free again before proceeding
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				System.out.println("Interrupted while waiting for NMX response");
				e.printStackTrace();
			}
		}
	}

}