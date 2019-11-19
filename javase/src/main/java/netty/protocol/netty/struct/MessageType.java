/*
Date: 04/29,2019, 20:21

消息类型
*/
package netty.protocol.netty.struct;

public enum MessageType {
    SERVICE_REQ((byte) 0),
    SERVICE_RESP((byte) 1),
    ONE_WAY((byte) 2),

    LOGIN_REQ((byte) 3),
    LOGIN_RESP((byte) 4),

    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RESP((byte) 6);

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte getvalue() {
        return this.value;
    }
}
