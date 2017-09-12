package com.arlen.common.sequence;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by arlen on 2017/9/12.
 */
public interface IBaseSequenceService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    BaseSequence getNewBaseSequence(byte seqKey);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    int getNextId(BaseSequence baseSequence);
}
