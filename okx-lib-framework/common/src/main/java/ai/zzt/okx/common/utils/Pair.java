package ai.zzt.okx.common.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * @author zhouzhitong
 * @since 2024/8/6
 **/
@Getter
@Setter
public class Pair<S, T> {

    private S first;

    private T second;

    public Pair() {
    }

    private Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<>(first, second);
    }

    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Pair<?, ?> pair)) {
            return false;
        }

        if (!ObjectUtils.nullSafeEquals(first, pair.first)) {
            return false;
        }

        return ObjectUtils.nullSafeEquals(second, pair.second);
    }

    @Override
    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(first);
        result = 31 * result + ObjectUtils.nullSafeHashCode(second);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s->%s", this.first, this.second);
    }

}
