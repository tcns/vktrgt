package ru.tcns.vktrgt.domain.external.vk.dict;

/**
 * Created by TIMUR on 01.05.2016.
 */
public enum Sex {
    M {
        @Override
        public int getIntValue() {
            return 2;
        }

        @Override
        public String toString() {
            return "Мужской";
        }
    },
    F {
        @Override
        public int getIntValue() {
            return 1;
        }

        @Override
        public String toString() {
            return "Женский";
        }
    },
    N {
        @Override
        public int getIntValue() {
            return 0;
        }

        @Override
        public String toString() {
            return "Не указано";
        }
    };
    public abstract int getIntValue();
    public Sex getByIntValue(Integer val) {
        for (Sex sex: Sex.values()) {
            if (sex.getIntValue()==val) {
                return sex;
            }
        }
        return N;
    }
}
