import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int displaySize; //«размер экрана»
    private JImageDisplay imageDisplay; //для обновления отображения в разных методах в процессе вычисления фрактала.
    private FractalGenerator fractalGenerator;// ссылка на базовый класс для отображения других видов фракталов в будущем.
    private Rectangle2D.Double range;// диапазона комплексной плоскости, которая выводится на экран.

    private FractalExplorer (int displaySize) //конструктор, который принимает значение
//   размера отображения в качестве аргумента, затем сохраняет это значение в
  //  соответствующем поле, а также инициализирует объекты диапазона и
    //фрактального генератора
    {
        this.displaySize = displaySize;
        this.fractalGenerator = new Mandelbrot();
        this.range = new Rectangle2D.Double(0,0,0,0);
        fractalGenerator.getInitialRange(this.range);
    }
    // задание интерфейса
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Generator");
        JButton button = new JButton("Reset");

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        imageDisplay.addMouseListener(new MouseListener());

        button.addActionListener(new ActionHandler());

        frame.setLayout(new java.awt.BorderLayout());//содержимого окна
        frame.add(imageDisplay, BorderLayout.CENTER); //добавьте объект отображения изображения в позици BorderLayout.CENTER
        frame.add(button, BorderLayout.SOUTH); //и кнопку в позицию BorderLayout.SOUTH.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //операцию закрытия окна по умолчанию
        frame.pack(); //правильное рахмещение содержимого
        frame.setVisible(true); //сделают его видимым
        frame.setResizable(false);// запрет изменения размеров окна
    }

    // отрисовка фрактала в JImageDisplay, вывод на эеран
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                int counter = fractalGenerator.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x),//вспомогательный метод//чтобы получить
                        //координату x, соответствующую координате пикселя X,
                        fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y));
                if (counter == -1) {
                    imageDisplay.drawPixel(x, y, 0);//Если число итераций равно -1 (т.е. точка не выходит за границы,
                   // установите пиксель в черный цвет
                }
                //поскольку значение цвета
                //варьируется от 0 до 1, получается плавная последовательность цветов от
                //красного к желтому, зеленому, синему, фиолетовому и затем обратно к
                //красному!
                else {
                    float hue = 0.7f + (float) counter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    imageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        imageDisplay.repaint(); //обновление изображения экрана
    }

    public class ActionHandler implements ActionListener {//внутренний класс для обработки событий
       // java.awt.event.ActionListener от кнопки сброса
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);//сбросить
            //диапазон к начальному, определенному генератором
            drawFractal();// перерисовать фрактал
        }
    }

    public class MouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            double x = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());//пиксельные координаты щелчка
            double y = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());
            fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);//метод генератора recenterAndZoomRange() с координатами, по которым
            //щелкнули, и масштабом 0.5.
            drawFractal();
        }
    }
    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);//Инициализировать новый экземпляр класса FractalExplorer с
      //  размером отображения 800.
        fractalExplorer.createAndShowGUI();//Вызовите метод createAndShowGUI () класса FractalExplorer.
        fractalExplorer.drawFractal();
    }
}