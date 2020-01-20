package de.hs.da.hskleinanzeigen;

import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

public class TestUtil {


    public static <T, ID> void mockFindById(CrudRepository<T, ID> repo, Iterator<T> iteratorMock, Iterator<ID> iteratorId) {
        while (iteratorId.hasNext() && iteratorMock.hasNext())
            given(repo.findById(iteratorId.next())).willReturn(Optional.of(iteratorMock.next()));
    }

    public static <T, ID> void mockFindById(CrudRepository<T, ID> repo, Collection<T> mocks, Function<T, ID> idMapper) {
        mockFindById(repo, mocks.iterator(), mocks.stream().map(idMapper).iterator());
    }


    public static <T> T checkArgs(T ref, Function<T, ?>... gettersToCheck){
        return checkArgs(ref, Arrays.asList(gettersToCheck));
    }
    public static <T> T checkArgs(T ref, Collection<Function<T, ?>> gettersToCheck){
        return argThat((T t) -> gettersToCheck.stream()
                .map(getter -> getter.apply(ref).equals(getter.apply(t)))
                .reduce(true, Boolean::logicalAnd));
    }
}
