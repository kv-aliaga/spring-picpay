package org.example.picpayapisimples;

import org.example.picpayapisimples.model.enums.TipoConta;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class PicPayApiSimplesApplicationTests {
    public static void main(String[] args) {
        System.out.println(TipoConta.valueOf("jones"));
    }
}
