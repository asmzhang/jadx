package jadx.gui.treemodel;

import jadx.api.ICodeInfo;
import jadx.api.impl.SimpleCodeInfo;
import jadx.core.utils.exceptions.JadxRuntimeException;
import jadx.gui.ui.codearea.AbstractCodeArea;
import jadx.gui.ui.codearea.CodeContentPanel;
import jadx.gui.ui.panel.ContentPanel;
import jadx.gui.ui.tab.TabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 可编辑的Smali类节点
 * 用于包装JClass并支持编辑反编译的Smali代码
 */
public class JEditableSmaliClass extends JEditableNode {
	private static final Logger LOG = LoggerFactory.getLogger(JEditableSmaliClass.class);

	private final JClass jClass;
	private final Path tempSmaliFile;
	private String originalSmaliCode;

	/**
	 * 构造函数
	 *
	 * @param jClass 原始的JClass节点
	 */
	public JEditableSmaliClass(JClass jClass) {
		this.jClass = jClass;
		try {
			this.tempSmaliFile = Files.createTempFile("jadx_smali_", ".smali");
			this.originalSmaliCode = jClass.getSmali();
			Files.write(tempSmaliFile, originalSmaliCode.getBytes());
		} catch (IOException e) {
			throw new JadxRuntimeException("Failed to create temp smali file", e);
		}
	}

	/**
	 * 获取原始的JClass节点
	 *
	 * @return 原始的JClass节点
	 */
	public JClass getJClass() {
		return jClass;
	}

	@Override
	public JPopupMenu onTreePopupMenu(jadx.gui.ui.MainWindow mainWindow) {
		return jClass.onTreePopupMenu(mainWindow);
	}

	@Override
	public ContentPanel getContentPanel(TabbedPane tabbedPane) {
		return new CodeContentPanel(tabbedPane, this);
	}

	@Override
	public String getSyntaxName() {
		return AbstractCodeArea.SYNTAX_STYLE_SMALI;
	}

	@Override
	public ICodeInfo getCodeInfo() {
		try {
			String smaliCode = new String(Files.readAllBytes(tempSmaliFile));
			return new SimpleCodeInfo(smaliCode);
		} catch (IOException e) {
			throw new JadxRuntimeException("Failed to read smali file: " + tempSmaliFile, e);
		}
	}

	/**
	 * 保存编辑后的Smali代码
	 *
	 * @param newContent 新的Smali代码内容
	 */
	@Override
	public void save(String newContent) {
		try {
			Files.write(tempSmaliFile, newContent.getBytes());
			setChanged(true);
			LOG.debug("Smali code saved to temp file: {}", tempSmaliFile);
		} catch (IOException e) {
			throw new JadxRuntimeException("Failed to write smali file: " + tempSmaliFile, e);
		}
	}

	/**
	 * 导出Smali代码到指定文件
	 *
	 * @param exportPath 导出路径
	 * @throws IOException 如果导出失败
	 */
	public void exportSmali(Path exportPath) throws IOException {
		String currentCode = getCurrentSmaliCode();
		Files.write(exportPath, currentCode.getBytes());
		LOG.debug("Smali code exported to: {}", exportPath);
	}

	/**
	 * 重置为原始的Smali代码
	 */
	public void reset() {
		try {
			Files.write(tempSmaliFile, originalSmaliCode.getBytes());
			setChanged(false);
			LOG.debug("Smali code reset to original");
		} catch (IOException e) {
			throw new JadxRuntimeException("Failed to reset smali file", e);
		}
	}

	/**
	 * 获取原始的Smali代码
	 *
	 * @return 原始的Smali代码
	 */
	public String getOriginalSmaliCode() {
		return originalSmaliCode;
	}

	/**
	 * 获取当前的Smali代码
	 *
	 * @return 当前的Smali代码
	 */
	public String getCurrentSmaliCode() {
		try {
			return new String(Files.readAllBytes(tempSmaliFile));
		} catch (IOException e) {
			throw new JadxRuntimeException("Failed to read current smali code", e);
		}
	}

	@Override
	public JClass getJParent() {
		return jClass.getJParent();
	}

	@Override
	public Icon getIcon() {
		return jClass.getIcon();
	}

	@Override
	public String makeString() {
		return jClass.makeString() + " (Editable)";
	}

	@Override
	public String getTooltip() {
		return jClass.getTooltip() + " - Editable Smali Mode";
	}

	/**
	 * 关闭资源
	 */
	public void close() {
		try {
			Files.deleteIfExists(tempSmaliFile);
		} catch (IOException e) {
			LOG.warn("Failed to delete temp smali file: {}", tempSmaliFile, e);
		}
	}

	@Override
	public int hashCode() {
		return jClass.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JEditableSmaliClass that = (JEditableSmaliClass) o;
		return jClass.equals(that.jClass);
	}
}
