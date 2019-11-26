package br.com.wallace.softplanchallenge.service.data.base.enums;


import br.com.wallace.softplanchallenge.service.data.strategy.LoginInfo;

import java.util.Arrays;
import java.util.Map;

public enum LoginTypeEnum implements LoginInfo {

    GIT_HUB {

        Map<String, Object> values;

        @Override
        public LoginInfo constructor(Map<String, Object> values) {
            this.values = values;
            return this;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return values;
        }

        @Override
        public String getId() {
            return ((Integer) values.get(FIELD_ID)).toString();
        }

        @Override
        public String getName() {
            return (String) values.get(FIELD_NAME);
        }

        @Override
        public String getEmail() {
            return (String) values.get(FIELD_EMAIL);
        }

        @Override
        public String getImagePath() {
            return (String) values.get(FIELD_AVATAR);
        }

        @Override
        public String getRegistrationId() {
            return "github";
        }

        @Override
        public LoginTypeEnum getType() {
            return LoginTypeEnum.GIT_HUB;
        }
    },
    FACEBOOK {
        private Map<String, Object> values;

        @Override
        public LoginInfo constructor(Map<String, Object> values) {
            this.values = values;
            return this;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return values;
        }

        @Override
        public String getId() {
            return ((Integer) values.get(FIELD_ID)).toString();
        }

        @Override
        public String getName() {
            return (String) values.get(FIELD_NAME);
        }

        @Override
        public String getEmail() {
            return (String) values.get(FIELD_EMAIL);
        }

        @Override
        public String getImagePath() {
            if (values.containsKey(FIELD_PICTURE)) {
                Map<String, Object> pictureObj = (Map<String, Object>) values.get(FIELD_PICTURE);
                if (pictureObj.containsKey(FIELD_DATA)) {
                    Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get(FIELD_DATA);
                    if (dataObj.containsKey(FIELD_URL)) {
                        return (String) dataObj.get(FIELD_URL);
                    }
                }
            }
            return null;
        }

        @Override
        public String getRegistrationId() {
            return "facebook";
        }

        @Override
        public LoginTypeEnum getType() {
            return LoginTypeEnum.FACEBOOK;
        }
    },
    GOOGLE {Map<String, Object> values;

        @Override
        public LoginInfo constructor(Map<String, Object> values) {
            this.values = values;
            return this;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return values;
        }

        @Override
        public String getId() {
            return ((Integer) values.get(FIELD_SUB)).toString();
        }

        @Override
        public String getName() {
            return (String) values.get(FIELD_NAME);
        }

        @Override
        public String getEmail() {
            return (String) values.get(FIELD_EMAIL);
        }

        @Override
        public String getImagePath() {
            return (String) values.get(FIELD_PICTURE);
        }

        @Override
        public String getRegistrationId() {
            return "google";
        }

        @Override
        public LoginTypeEnum getType() {
            return LoginTypeEnum.GOOGLE;
        }
    },
    DEFAULT {
        @Override
        public LoginInfo constructor(Map<String, Object> values) {
            return null;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getImagePath() {
            return null;
        }

        @Override
        public LoginTypeEnum getType() {
            return LoginTypeEnum.DEFAULT;
        }

        @Override
        public String getRegistrationId() {
            return "default";
        }
    };

    private static final String FIELD_SUB = "sub";
    private static final String FIELD_URL = "url";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_PICTURE = "picture";
    private static final String FIELD_AVATAR = "avatar_url";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ID = "id";


    public static LoginTypeEnum getFromValue(String value) {
        return Arrays.stream(LoginTypeEnum.values())
                .filter(enm -> enm.getRegistrationId().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
