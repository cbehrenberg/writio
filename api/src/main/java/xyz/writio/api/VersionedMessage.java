package xyz.writio.api;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

/**
 * <p>
 * Encapsulates a protobuf3 message and allows versioned serialization to JSON.
 * </p>
 * <p>
 * Encapsulate with {@link #VersionedMessage(Message)} and serialize to JSON
 * with {@link #getJsonStr()}.
 * </p>
 * 
 * @author <a href="mailto:christian.behrenberg@gmail.com">Christian
 *         Behrenberg</a>
 */
public class VersionedMessage {

	/**
	 * Any-type message encapsulation of protobuf3 message. Enables the versioning
	 * on @type field.
	 */
	private Any anyMessage;

	/**
	 * Materialized JSON string of Versioned Message.
	 */
	private String jsonStr;

	/**
	 * Converter for Protobuf to JSON
	 */
	private ProtobufJsonStrConverter protobufJsonStrConverter;

	/**
	 * Versioned message must be constructed with the message to be versioned! Use
	 * {@link #VersionedMessage(Message)} instead.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private VersionedMessage() {
	}

	/**
	 * Creates Versioned Message representation for protobuf3 message
	 * 
	 * @param message Protobuf3 message
	 */
	public VersionedMessage(Message message) {
		assert message != null;
		this.anyMessage = Any.pack(message);
	}

	/**
	 * @return serialization as JSON string
	 * @implNote cached lazy loading
	 */
	public String getJsonStr() throws InvalidProtocolBufferException {

		if (this.jsonStr == null) {
			this.jsonStr = getProtobufJsonStrConverter().convertFromSrc(anyMessage);
		}

		return this.jsonStr;
	}

	/**
	 * @return protobuf to JSON string converter
	 * @implNote cached lazy loading
	 */
	private ProtobufJsonStrConverter getProtobufJsonStrConverter() {

		if (this.protobufJsonStrConverter == null) {
			this.protobufJsonStrConverter = new ProtobufJsonStrConverter();
		}

		return this.protobufJsonStrConverter;
	}

	/**
	 * @return Versioned type
	 */
	public String getVersionType() {
		return this.anyMessage.getTypeUrl();
	}
}
