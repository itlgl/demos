package com.itlgl.markdown;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;

public class App {

	JTextArea jt_markdown = null, jt_html = null;
	JButton jb_ok = null;

	public static void main(String[] args) {
		new App().showUI();
	}

	void showUI() {
		// 创建及设置窗口
		JFrame frame = new JFrame("Markdown To HTML");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 600);
		frame.setLayout(null);
		{
			jt_markdown = new JTextArea(10, 10);
			jt_markdown.setText("input markdown text here...");
			jt_markdown.setLineWrap(true);// 如果内容过长。自动换行
			JScrollPane js_markdown = new JScrollPane(jt_markdown, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			js_markdown.setBounds(0, 0, 450, 560);
			frame.add(js_markdown);
		}
		{
			jt_html = new JTextArea(10, 10);
			jt_html.setText("html");
			// jt_html.setLineWrap(true);// 如果内容过长。自动换行
			JScrollPane js_html = new JScrollPane(jt_html, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			js_html.setBounds(550, 0, 430, 560);
			frame.add(js_html);
		}
		{
			JButton jb_load = new JButton("<<load");
			jb_load.setBounds(460, 240, 80, 30);
			jb_load.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.showDialog(new JLabel(), "选择一个markdown文件");
					try {
						File file=jfc.getSelectedFile();
						if(file == null) {
							return;
						}
						InputStream is = new FileInputStream(file);
						byte[] buffer = new byte[4096];
						int len;
						ByteArrayOutputStream baos = new ByteArrayOutputStream((int)file.length());
						while((len = is.read(buffer)) != -1) {
							baos.write(buffer, 0, len);
						}
						is.close();
						jt_markdown.setText(baos.toString("utf-8"));
					} catch (Exception e1) {
						e1.printStackTrace();
						jt_markdown.setText("加载文件失败:" + e1);
					}
				}
			});
			frame.add(jb_load);
			
			jb_ok = new JButton("转换>>");
			jb_ok.setBounds(460, 285, 80, 30);
			jb_ok.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					convertMarkdownToHtml();
				}
			});
			frame.add(jb_ok);
		}
		{
			JButton jb_copy = new JButton("复制生成的HTML");
		}
		// 显示窗口
		// frame.pack();
		frame.setVisible(true);
	}

	void convertMarkdownToHtml() {
		String markdownStr = jt_markdown.getText();
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdownStr);
		HtmlRenderer renderer = HtmlRenderer.builder().nodeRendererFactory(new HtmlNodeRendererFactory() {
			public NodeRenderer create(HtmlNodeRendererContext context) {
				return new WxNodeRender(context);
			}
		}).build();
		String htmlStr = renderer.render(document);
		jt_html.setText(htmlStr);
	}
}
