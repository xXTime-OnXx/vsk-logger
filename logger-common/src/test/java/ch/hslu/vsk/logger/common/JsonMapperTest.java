package ch.hslu.vsk.logger.common;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapperTest {

    @Test
    void testFromString() {
        String testObjectJson = "{\"attribute1\":\"attribute1\",\"attribute2\":[2024,4,11],\"attribute3\":3}";
        JsonMapperTestObject testObject = new JsonMapperTestObject();
        testObject.setAttribute1("attribute1");
        testObject.setAttribute2(LocalDate.of(2024, 4, 11));
        testObject.setAttribute3(3);

        String result = JsonMapper.toString(testObject);

        assertEquals(testObjectJson, result);
    }

    @Test
    void testToString() {
        String testObjectJson = "{\"attribute1\":\"attribute1\",\"attribute2\":[2024,4,11],\"attribute3\":3}";
        JsonMapperTestObject testObject = new JsonMapperTestObject();
        testObject.setAttribute1("attribute1");
        testObject.setAttribute2(LocalDate.of(2024, 4, 11));
        testObject.setAttribute3(3);

        JsonMapperTestObject result = JsonMapper.fromString(testObjectJson, JsonMapperTestObject.class);

        assertEquals(testObject, result);
    }

    static class JsonMapperTestObject {
        private String attribute1;
        private LocalDate attribute2;
        private int attribute3;

        public String getAttribute1() {
            return attribute1;
        }

        public void setAttribute1(String attribute1) {
            this.attribute1 = attribute1;
        }

        public LocalDate getAttribute2() {
            return attribute2;
        }

        public void setAttribute2(LocalDate attribute2) {
            this.attribute2 = attribute2;
        }

        public int getAttribute3() {
            return attribute3;
        }

        public void setAttribute3(int attribute3) {
            this.attribute3 = attribute3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JsonMapperTestObject that)) return false;
            return attribute3 == that.attribute3 && Objects.equals(attribute1, that.attribute1) && Objects.equals(attribute2, that.attribute2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attribute1, attribute2, attribute3);
        }
    }

}