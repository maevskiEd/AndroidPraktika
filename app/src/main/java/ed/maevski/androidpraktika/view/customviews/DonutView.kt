package ed.maevski.androidpraktika.view.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ed.maevski.androidpraktika.R

class DonutView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {
    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()

    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    //Толщина линии прогресса
    private var stroke = 10f

    //Значение прогресса от 0 - 100
    private var progress = 50

    //Значения размера текста внутри кольца
    private var scaleSize = 32f

    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    private fun initPaint() {
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when (progress) {
        in 0..1000 -> Color.parseColor("#e84258")
        in 1001..10000 -> Color.parseColor("#fd8060")
        in 10001..100000 -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }

    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.DonutView, 0, 0)
        try {
            stroke = a.getFloat(
                R.styleable.DonutView_stroke, stroke
            )
            progress = a.getInt(R.styleable.DonutView_progress, progress)
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    private fun drawDonut(canvas: Canvas) {
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * 0.8f
        //Сохраняем канвас
        canvas.save()
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        canvas.translate(centerX, centerY)
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        canvas.drawArc(oval, -90f, convertProgressToDegrees(progress), false, strokePaint)
        //Восстанавливаем канвас
        canvas.restore()
    }

    private fun convertProgressToDegrees(progress: Int): Float {
        val arc: Float = when (progress) {
            in 0..1000 -> 0.9f * (progress / 10)
            in 1001..10000 -> 90f + (0.9f * (progress / 100))
            in 10001..100000 -> 180f + (0.9f * (progress / 1000))
            else -> 270f + (0.9f * (progress / 10000))
        }
        return arc
    }

    private fun drawText(canvas: Canvas) {
        var message: String
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
//        val message = String.format("%.1f", progress / 10f)
        if (progress >= 1000) {
            message = (progress / 1000).toString() + "K"
        } else {
            message = progress.toString()
        }
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / 2, centerY + advance / 4, digitPaint)
    }

    override fun onDraw(canvas: Canvas) {
        //Рисуем кольцо и задний фон
        drawDonut(canvas)
        //Рисуем цифры
        drawText(canvas)
    }

    fun setProgress(pr: Int) {
        //Кладем новое значение в наше поле класса
        progress = pr
        //Создаем краски с новыми цветами
        initPaint()
        //вызываем перерисовку View
        invalidate()
    }
}