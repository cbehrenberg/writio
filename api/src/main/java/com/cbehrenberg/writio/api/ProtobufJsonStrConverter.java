package com.cbehrenberg.writio.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import com.google.protobuf.util.JsonFormat.TypeRegistry;

public class ProtobufJsonStrConverter extends AbstractConverter<Message, String> {

	private static final Logger logger = LoggerFactory.getLogger(ProtobufJsonStrConverter.class);

	public ProtobufJsonStrConverter() {
		super(ProtobufJsonStrConverter::messageToJsonString, ProtobufJsonStrConverter::jsonStrToMessage);
	}

	/**
	 * Converts protobuf3 message to JSON string
	 * 
	 * @param message Protobuf3 message
	 * 
	 * @return JSON string
	 * 
	 * @implNote Uses protobuf internal methods to serialize to JSON. However, some
	 *           unicode characters are escaped in that process, hence after
	 *           serialization, the text is de-escaped again to preserve the
	 *           original encoding.
	 */
	private static String messageToJsonString(Message message) {

		List<Descriptor> descriptors = new ArrayList<Descriptor>();

		Reflections reflections = new Reflections(VersionedMessage.class.getPackageName());

		// lookup subtypes from protobuf class Message
		Set<Class<? extends Message>> messageClasses = reflections.getSubTypesOf(Message.class);
		logger.debug("found {} classes as subtype of {}", messageClasses.size(), Message.class.getCanonicalName());

		// check each class if final and has getDescriptor
		// if yes: add descriptor to list
		for (Class<? extends Message> messageClass : messageClasses) {

			int classModifiers = messageClass.getModifiers();

			if (!Modifier.isInterface(classModifiers) && !Modifier.isAbstract(classModifiers)
					&& Modifier.isFinal(classModifiers)) {

				try {

					Method getDescriptor = messageClass.getMethod("getDescriptor");
					assert getDescriptor != null;

					Descriptor messageDescriptor = (Descriptor) getDescriptor.invoke(messageClass);
					assert messageDescriptor != null;

					descriptors.add(messageDescriptor);
					logger.debug("adding class {} to descriptor list", messageClass.getCanonicalName());

				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.warn("Cannot access class {} getDescriptor method: {}", messageClass.getCanonicalName(),
							e.getMessage());
				}
			} else {
				logger.debug("skipping class {} for descriptor", messageClass.getCanonicalName());
			}
		}

		TypeRegistry typeRegistry = TypeRegistry.newBuilder().add(descriptors).build();

		Printer jsonSerializer = JsonFormat.printer().usingTypeRegistry(typeRegistry);

		// serialize protobuf to json
		String jsonStr = null;
		try {
			jsonStr = jsonSerializer.print(message);
		} catch (InvalidProtocolBufferException e) {
			throw new IllegalArgumentException(e);
		}

		// unescape unicode characters that weren't preserved
		String jsonUnescapedStr = StringEscapeUtils.unescapeJson(jsonStr);

		return jsonUnescapedStr;
	}

	/**
	 * Converts JSON string to protobuf3 message TODO: Implement
	 * 
	 * @param jsonStr
	 * @return Protobuf3 message
	 */
	private static Message jsonStrToMessage(String jsonStr) {
		return null;
	}
}
