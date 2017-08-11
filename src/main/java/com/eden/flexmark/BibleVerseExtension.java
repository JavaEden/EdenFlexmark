package com.eden.flexmark;

import com.eden.repositories.EdenRepository;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataHolder;

public class BibleVerseExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    public static final DataKey<Class<? extends EdenRepository>> BIBLE_REPOSITORY = new DataKey<>("BIBLE_REPOSITORY", (Class<? extends EdenRepository>) null);

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
