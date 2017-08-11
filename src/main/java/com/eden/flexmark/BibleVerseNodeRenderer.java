package com.eden.flexmark;

import com.caseyjbrooks.clog.Clog;
import com.eden.Eden;
import com.eden.bible.Passage;
import com.eden.repositories.EdenRepository;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.sequence.BasedSequence;

import java.util.HashSet;
import java.util.Set;

public class BibleVerseNodeRenderer implements NodeRenderer {

    private final Class<? extends EdenRepository> bibleRepository;

    public BibleVerseNodeRenderer(DataHolder options) {
        this.bibleRepository = options.get(BibleVerseExtension.BIBLE_REPOSITORY);

        if(this.bibleRepository != null) {
            Clog.v("Bible repository Class: {}", this.bibleRepository.toString());
        }
        else {
            Clog.v("Bible repository Class: is null");
        }
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(BibleVerse.class, BibleVerseNodeRenderer.this::render));
        return set;
    }

    private void render(final BibleVerse node, final NodeRendererContext context, final HtmlWriter html) {
        final BasedSequence sourceText = context.getHtmlOptions().sourcePositionParagraphLines || node.getFirstChild() == null ? node.getChars() : node.getFirstChild().getChars();

        html.srcPos(sourceText.getStartOffset(), sourceText.getEndOffset()).tagLine("span", () -> {
            String output = node.getText().toString();

            if(this.bibleRepository != null) {
                Clog.v("Bible repository Class: is null");
                Eden eden = Eden.getInstance();
                EdenRepository repo = eden.getRepository(this.bibleRepository);
                if(repo == null) {
                    Clog.v("Did not have an existing Repository registered");
                    try {
                        eden.registerRepository(this.bibleRepository.newInstance());
                        Clog.v("Successfully created and registered a repository");
                        repo = eden.getRepository(this.bibleRepository);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        Clog.v("Failed to create and register a repository");
                    }
                }

                if(repo != null) {
                    Clog.v("Repository exists");
                    Passage passage = repo.lookupVerse(node.getText().toString());
                    output = passage.getText() + " ~ " + passage.getReference().toString();
                }
            }

            html.raw(output);
        });

    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new BibleVerseNodeRenderer(options);
        }
    }
}
