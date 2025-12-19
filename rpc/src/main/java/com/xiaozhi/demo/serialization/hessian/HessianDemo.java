package com.xiaozhi.demo.serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.xiaozhi.demo.serialization.protobuf.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author DD
 */
public class HessianDemo {

    public static void main(String[] args) throws IOException {
        Test test = Test.newBuilder()
                .setName("xiaozhi")
                .setAge(18)
                .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output h2o = new Hessian2Output(baos);
        h2o.writeObject(test);
        h2o.flush();
        byte[] byteArray = baos.toByteArray();
        System.out.println(byteArray.length);

        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        Hessian2Input h2i = new Hessian2Input(bais);
        Object readObject = h2i.readObject();
        System.out.println(readObject);
    }
}
