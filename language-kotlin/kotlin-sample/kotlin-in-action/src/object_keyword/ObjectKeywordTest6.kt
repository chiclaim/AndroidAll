package object_keyword

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel


/**
 * Desc: object表达式 创建匿名内部类 演示
 * Created by Chiclaim on 2018/9/20.
 */


fun main(args: Array<String>) {

    val jLabel = JLabel()

    jLabel.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            super.mouseClicked(e)
            println("mouseClicked")
        }

        override fun mouseMoved(e: MouseEvent?) {
            super.mouseMoved(e)
            println("mouseMoved")
        }
    })
}

/*

上面代码对应的Java代码：

public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      JLabel jLabel = new JLabel();
      jLabel.addMouseListener((MouseListener)(new MouseAdapter() {
         public void mouseClicked(@Nullable MouseEvent e) {
            super.mouseClicked(e);
            String var2 = "mouseClicked";
            System.out.println(var2);
         }

         public void mouseMoved(@Nullable MouseEvent e) {
            super.mouseMoved(e);
            String var2 = "mouseMoved";
            System.out.println(var2);
         }
      }));
   }


 */