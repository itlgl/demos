package com.itlgl.markdown;

import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.ListBlock;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;
import org.commonmark.renderer.html.CoreHtmlNodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

/**
 * 生成适合微信格式的html5的render
 * 现在的格式按照‘移动开发前线’的公众号文章格式修改
 * 各个处理的代码可以参考org.commonmark.renderer.html.CoreHtmlNodeRenderer的代码
 * @author itlgl
 *
 */
public class WxNodeRender extends CoreHtmlNodeRenderer {
	private final HtmlWriter _html;
	
	public WxNodeRender(HtmlNodeRendererContext context) {
		super(context);
        this._html = context.getWriter();
    }
	
	// 标题
	@Override
    public void visit(Heading heading) {
		System.out.println("handle heading...");
		String htag = "h" + heading.getLevel();
		_html.line();
		_html.raw("<section class=\"");
		_html.raw(htag);
		_html.raw("\" style=\"box-sizing: border-box;font-family: Avenir, -apple-system-font, 微软雅黑, sans-serif;line-height: 1.1;color: rgb(60, 112, 198);margin-top: 30px;margin-left: 8px;font-size: 16px;white-space: normal;background-color: rgb(255, 255, 255);\">");
		_html.line();
		_html.raw(" <span style=\"box-sizing: border-box;display: inline-block;width: 15px;height: 15px;margin-right: 10px;background: url(&quot;http://mmbiz.qpic.cn/mmbiz_png/ibnDKD3ktAoZzDJlBXvft8TIiajyCxfwJcr2k6BibbeXgiag62A7qesxxc7Bqpot5XUTZ2iaqTL7Ta9NlIOnvVvliaew/0?wx_fmt=png&quot;) center center / 100% 100% no-repeat;\"></span>&nbsp;");
		_html.line();
		_html.raw(" <span style=\"box-sizing: border-box;\">");
		
		visitChildren(heading);
		
		_html.tag("/span");
		_html.line();
		_html.tag("/section");
		_html.line();
    }
	
	// 段落，每一个段落都会回调这个方法
	@Override
	public void visit(Paragraph paragraph) {
		System.out.println("handle paragraph,parent=" + paragraph.getParent());
		// 如果段落中只有文字，那么给文字加一个style
		Node firstChild = paragraph.getFirstChild();
		if(paragraph.getParent() instanceof Document && firstChild != null && firstChild instanceof Text && firstChild.getFirstChild() == null) {
			boolean inTightList = isInTightList(paragraph);
	        if (!inTightList) {
	            _html.line();
	            _html.raw("<p style=\"box-sizing: border-box;font-family: Avenir, -apple-system-font, 微软雅黑, sans-serif;background-color: rgb(255, 255, 255);font-size: 16px;text-align: justify;white-space: pre-line;padding-top: 23px;padding-right: 8px;padding-left: 8px;color: rgb(74, 74, 74);line-height: 1.75em;\">");
	        }
	        visitChildren(paragraph);
	        if (!inTightList) {
	            _html.tag("/p");
	            _html.line();
	        }
		} else {
			super.visit(paragraph);
		}
	}
	
	@Override
	public void visit(BlockQuote blockQuote) {
		System.out.println("handle blockQuote");
		_html.line();
		_html.raw("<section class=\"preface\" style=\"box-sizing: border-box;font-family: Avenir, -apple-system-font, 微软雅黑, sans-serif;white-space: normal;font-size: 15px;text-align: justify;line-height: 27px;color: rgb(89, 89, 89);background-color: rgb(239, 239, 239);padding: 19px;margin-top: 40px;margin-right: 8px;margin-left: 8px;\">");
		_html.line();
		visitChildren(blockQuote);
		_html.line();
		_html.tag("/section");
		_html.line();
	}
	
	@Override
    public void visit(BulletList bulletList) {
		System.out.println("handle bulletList,[" + bulletList.getBulletMarker());
		super.visit(bulletList);
	}
	
	// 独立的代码
	@Override
	public void visit(FencedCodeBlock fencedCodeBlock) {
		System.out.println("handle fencedCodeBlock,独立的代码,[" + fencedCodeBlock.getLiteral());
		_html.line();
        _html.raw("<pre style=\"box-sizing: border-box;overflow: auto;font-size: 13px;padding: 9.5px;margin: 30px 8px 0px;line-height: 1.42857;word-break: break-all;word-wrap: break-word;color: rgb(91, 172, 235);background-color: rgb(50, 50, 50);border-width: 1px;border-style: solid;border-color: rgb(204, 204, 204);border-radius: 4px;\"><code style=\"box-sizing: border-box;\">");
        _html.text(fencedCodeBlock.getLiteral());
        _html.tag("/code");
        _html.tag("/pre");
        _html.line();
	}
	
	@Override
	public void visit(HtmlBlock htmlBlock) {
		System.out.println("handle htmlBlock,[" + htmlBlock.getLiteral());
		super.visit(htmlBlock);
	}
	
	@Override
	public void visit(ThematicBreak thematicBreak) {
		System.out.println("分割线<hr>");
		super.visit(thematicBreak);
	}
	
	// 独立代码
	@Override
	public void visit(IndentedCodeBlock indentedCodeBlock) {
		System.out.println("<pre><code>独立代码");
		_html.line();
		_html.raw("<pre style=\"box-sizing: border-box;overflow: auto;font-size: 13px;padding: 9.5px;margin: 30px 8px 0px;line-height: 1.42857;word-break: break-all;word-wrap: break-word;color: rgb(91, 172, 235);background-color: rgb(50, 50, 50);border-width: 1px;border-style: solid;border-color: rgb(204, 204, 204);border-radius: 4px;\"><code style=\"box-sizing: border-box;\">");
		_html.text(indentedCodeBlock.getLiteral());
		_html.tag("/code");
        _html.tag("/pre");
        _html.line();
	}
	
	@Override
    public void visit(ListItem listItem) {
		System.out.println("无序列表");
		_html.tag("li");
		_html.raw("<p style=\"box-sizing: border-box;font-size: 16px;text-align: justify;white-space: pre-line;color: rgb(74, 74, 74);line-height: 1.75em;\">");
        visitChildren(listItem);
        _html.tag("/p");
        _html.tag("/li");
        _html.line();
	}
	
	@Override
    public void visit(OrderedList orderedList) {
		System.out.println("有序列表");
		super.visit(orderedList);
	}
	
	@Override
	public void visit(Image image) {
		System.out.println("图片,[" + image.getTitle() + "," + image.getDestination());
		super.visit(image);
	}
	
	// 斜体
	@Override
    public void visit(Emphasis emphasis) {
		System.out.println("斜体,[" + emphasis.getOpeningDelimiter());
		super.visit(emphasis);
	}
	
	@Override
    public void visit(StrongEmphasis strongEmphasis) {
		System.out.println("粗体,[" + strongEmphasis.getOpeningDelimiter());
		super.visit(strongEmphasis);
	}
	
	@Override
    public void visit(Text text) {
		System.out.println("visit text,[" + text.getLiteral() + ",parent=" + text.getParent());
		super.visit(text);
	}
	
	@Override
    public void visit(Code code) {
		System.out.println("visit code,[" + code.getLiteral());
		_html.raw("<code style=\"box-sizing: border-box;\">");
		_html.text(code.getLiteral());
	    _html.tag("/code");
	}
	
	@Override
    public void visit(HtmlInline htmlInline) {
		System.out.println("visit htmlInline,[" + htmlInline.getLiteral());
		super.visit(htmlInline);
	}
	
	@Override
	public void visit(SoftLineBreak softLineBreak) {
		System.out.println("软换行");
		super.visit(softLineBreak);
	}
	
	@Override
	public void visit(HardLineBreak hardLineBreak) {
		System.out.println("强制换行");
		super.visit(hardLineBreak);
	}
	
	private boolean isInTightList(Paragraph paragraph) {
        Node parent = paragraph.getParent();
        if (parent != null) {
            Node gramps = parent.getParent();
            if (gramps != null && gramps instanceof ListBlock) {
                ListBlock list = (ListBlock) gramps;
                return list.isTight();
            }
        }
        return false;
    }
}
