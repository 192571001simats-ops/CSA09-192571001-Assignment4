import javax.swing.SwingUtilities;
public class ConverterStyleLauncher {
 public static void main(String[] args){
  String style=args.length==0?"lambda":args[0];
  SwingUtilities.invokeLater(()->{
   CurrencyView view=switch(style){case "frame"->new FrameConverter();case "named"->new NamedConverter();case "anonymous"->new AnonymousConverter();case "lambda"->new LambdaConverter();default->throw new IllegalArgumentException("Use frame, named, anonymous or lambda");};
   view.setVisible(true);
  });
 }
}
