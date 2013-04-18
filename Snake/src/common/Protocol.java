package common;


public class Protocol {
	public static final int 
	COM_TURN_LEFT 	= 1,
	COM_TURN_RIGHT 	= 2,
	COM_SEND_STATE 	= 3,
	COM_SEND_POSITION = 4,
	COM_END 		= 5,

	ANS_TURN_LEFT 	= 21,
	ANS_TURN_RIGHT 	= 22,
	ANS_SEND_STATE 	= 23,
	ANS_SEND_POSITION = 49,
	ANS_ACK 		= 24,
	ANS_NACK 		= 25,
	ANS_END 		= 26;

	public static String codeString(int code) {
		switch (code) {
			case COM_TURN_LEFT:    	return "COM_TURN_LEFT";
			case COM_TURN_RIGHT: 	return "COM_TURN_RIGHT";
			case COM_SEND_STATE:	return "COM_SEND_STATE";
			case COM_END:			return "COM_END";
			case ANS_TURN_LEFT:		return "ANS_TURN_LEFT";
			case ANS_TURN_RIGHT:	return "ANS_TURN_RIGHT";
			case ANS_SEND_STATE:	return "ANS_SEND_STATE";
			case ANS_ACK:			return "ANS_ACK";
			case ANS_NACK:			return "ANS_NACK";
			case ANS_END:			return "ANS_END";
			default: return "";
		}
	}
}
