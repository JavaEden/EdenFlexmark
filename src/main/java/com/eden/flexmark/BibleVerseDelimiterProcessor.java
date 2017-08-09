package com.eden.flexmark;

import com.vladsch.flexmark.ast.DelimitedNode;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.internal.Delimiter;
import com.vladsch.flexmark.parser.InlineParser;
import com.vladsch.flexmark.parser.delimiter.DelimiterProcessor;
import com.vladsch.flexmark.parser.delimiter.DelimiterRun;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class BibleVerseDelimiterProcessor implements DelimiterProcessor {

    @Override
    public char getOpeningCharacter() {
        return '(';
    }

    @Override
    public char getClosingCharacter() {
        return ')';
    }

    @Override
    public int getMinLength() {
        return 2;
    }

    @Override
    public int getDelimiterUse(DelimiterRun opener, DelimiterRun closer) {
        return getMinLength();
    }

    @Override
    public void process(Delimiter opener, Delimiter closer, int delimitersUsed) {
        DelimitedNode verse = new BibleVerse(opener.getTailChars(delimitersUsed), BasedSequence.NULL, closer.getLeadChars(delimitersUsed));

        opener.moveNodesBetweenDelimitersTo(verse, closer);
    }

    @Override
    public boolean canBeOpener(boolean leftFlanking, boolean rightFlanking, boolean beforeIsPunctuation, boolean afterIsPunctuation, boolean beforeIsWhitespace, boolean afterIsWhiteSpace) {
        return leftFlanking;
    }

    @Override
    public boolean canBeCloser(boolean leftFlanking, boolean rightFlanking, boolean beforeIsPunctuation, boolean afterIsPunctuation, boolean beforeIsWhitespace, boolean afterIsWhiteSpace) {
        return rightFlanking;
    }

    @Override
    public Node unmatchedDelimiterNode(InlineParser inlineParser, final DelimiterRun delimiter) {
        return null;
    }
}
