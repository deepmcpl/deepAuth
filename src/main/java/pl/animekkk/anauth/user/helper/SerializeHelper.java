package pl.animekkk.anauth.user.helper;

import java.io.*;
import java.util.Base64;

public class SerializeHelper {

    public static String serialize(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static Object deserialize(String serialized) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(serialized)));
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }


}
