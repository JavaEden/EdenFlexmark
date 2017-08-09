package com.eden.flexmark;

import com.vladsch.flexmark.ast.DelimitedNodeImpl;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class BibleVerse extends DelimitedNodeImpl {

    public BibleVerse() {
    }

    public BibleVerse(BasedSequence chars) {
        super(chars);
    }

    public BibleVerse(BasedSequence openingMarker, BasedSequence content, BasedSequence closingMarker) {
        super(openingMarker, content, closingMarker);
    }

}