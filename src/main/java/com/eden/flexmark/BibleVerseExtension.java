package com.eden.flexmark;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;

public class BibleVerseExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private BibleVerseExtension() {
    }

    public static Extension create() {
        return new BibleVerseExtension();
    }

// Parser Extension implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void parserOptions(final MutableDataHolder options) {

    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new BibleVerseDelimiterProcessor());
    }

// Renderer Extension implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void rendererOptions(MutableDataHolder options) {

    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererType.equals("HTML")) {
            rendererBuilder.nodeRendererFactory(new BibleVerseNodeRenderer.Factory());
        }
    }
}
