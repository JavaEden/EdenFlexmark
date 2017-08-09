package com.eden.flexmark;

import com.eden.Eden;
import com.eden.americanbiblesociety.ABSRepository;
import com.eden.bible.AbstractVerse;
import com.eden.bible.Passage;
import com.eden.interfaces.VerseFormatter;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Set;

public class BibleVerseNodeRenderer implements NodeRenderer {

    private DataHolder options;

    public BibleVerseNodeRenderer(DataHolder options) {
        this.options = options;
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
            Eden eden = Eden.getInstance();

            String output;

            eden.registerRepository(new ABSRepository());
            ABSRepository repo = (ABSRepository) eden.getRepository(ABSRepository.class);

            Passage passage = repo.lookupVerse(node.getText().toString());
            passage.setVerseFormatter(new VerseFormatter() {
                @Override
                public String onPreFormat(AbstractVerse abstractVerse) {
                    return "“";
                }

                @Override
                public String onFormatVerseStart(int i) {
                    return "";
                }

                @Override
                public String onFormatText(String s) {
                    Document doc = Jsoup.parse(s);
                    doc.select("sup").remove();
                    s = doc.text();

                    if(s.startsWith("“")) {
                        s = s.substring(1);
                    }
                    if(s.endsWith("”")) {
                        s = s.substring(0, s.length() - 1);
                    }

                    return s;
                }

                @Override
                public String onFormatVerseEnd() {
                    return " ";
                }

                @Override
                public String onPostFormat() {
                    return "”";
                }
            });

            output = passage.getText().split("<br/><i>")[0] + " ~ " + passage.getReference().toString();

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
