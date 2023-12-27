package com.ljx.indicator

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.ljx.indicator.base.BaseAdapter
import com.ljx.indicator.databinding.IndicatorLayoutBinding
import com.ljx.indicator.databinding.NumberAdapterBinding
import com.ljx.view.IndicatorView

/**
 * User: ljx
 * Date: 2023/12/24
 * Time: 14:57
 */
class IndicatorActivity : FragmentActivity(), View.OnClickListener {
    private lateinit var binding: IndicatorLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        binding = DataBindingUtil.setContentView(this, R.layout.indicator_layout)
        binding.init()
    }

    private fun IndicatorLayoutBinding.init() {
        mutableListOf(
            ivPrimaryMinus, ivPrimaryPlus, ivPrimaryCheckedMinus, ivPrimaryCheckedPlus,
            ivSecondaryMinus, ivSecondaryPlus, ivSecondaryCheckedMinus, ivSecondaryCheckedPlus,
            ivGapMinus, ivGapPlus, ivRoundRadiusMinus, ivRoundRadiusPlus
        ).forEach { it.setOnClickListener(this@IndicatorActivity) }

        val dataList = mutableListOf<Int>()
        for (i in 0 until 5) {
            dataList.add(i)
        }

        viewPager2.adapter = NumberAdapter(dataList)
        getIndicatorViews().forEach { it.setupWithViewPager2(viewPager2) }

        rgOrientation.setOnCheckedChangeListener { _, checkedId ->
            changeOrientation(checkedId)
        }
        rgLayoutDirection.setOnCheckedChangeListener { _, checkedId ->
            changeLayoutDirection(checkedId)
        }
        rgReverseLayout.setOnCheckedChangeListener { _, checkedId ->
            changeReverseLayout(checkedId)
        }
        rgHorizontalGravity.setOnCheckedChangeListener { _, _ ->
            changeGravity()
        }
        rgVerticalGravity.setOnCheckedChangeListener { _, _ ->
            changeGravity()
        }

        etPrimarySize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setPrimarySize(editable.getInt(0).dp)
            }
        }
        etPrimaryCheckedSize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setPrimaryCheckedSize(editable.getInt(0).dp)
            }
        }

        etSecondarySize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setSecondarySize(editable.getInt(0).dp)
            }
        }
        etSecondaryCheckedSize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setSecondaryCheckedSize(editable.getInt(0).dp)
            }
        }

        etGapSize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setSliderGap((editable.toString().toIntOrNull() ?: 0).dp)
            }
        }
        etRoundRadiusSize.addTextChangedListener { editable ->
            getIndicatorViews().forEach {
                it.setRoundRadius(editable.getInt(0).dp.toFloat())
            }
        }
    }

    private fun changeOrientation(checkedId: Int) {
        val orientation =
            if (checkedId == R.id.rb_horizontal) IndicatorView.HORIZONTAL else IndicatorView.VERTICAL
        getIndicatorViews().forEach { it.setOrientation(orientation) }
    }

    private fun changeLayoutDirection(checkedId: Int) {
        val layoutDirection =
            if (checkedId == R.id.rb_ltr) View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
        getIndicatorViews().forEach { it.layoutDirection = layoutDirection }
    }

    private fun changeReverseLayout(checkedId: Int) {
        val reverseLayout = checkedId == R.id.rb_yes
        getIndicatorViews().forEach { it.setReverseLayout(reverseLayout) }
    }


    private fun IndicatorLayoutBinding.changeGravity() {
        val horizontalGravity = when (rgHorizontalGravity.checkedRadioButtonId) {
            R.id.rb_horizontal_left -> Gravity.LEFT
            R.id.rb_horizontal_start -> Gravity.START
            R.id.rb_horizontal_center -> Gravity.CENTER_HORIZONTAL
            R.id.rb_horizontal_end -> Gravity.END
            R.id.rb_horizontal_right -> Gravity.RIGHT
            else -> 0
        }
        val verticalGravity = when (rgVerticalGravity.checkedRadioButtonId) {
            R.id.rb_vertical_top -> Gravity.TOP
            R.id.rb_vertical_center -> Gravity.CENTER_VERTICAL
            R.id.rb_vertical_bottom -> Gravity.BOTTOM
            else -> 0
        }

        val gravity = horizontalGravity or verticalGravity
        getIndicatorViews().forEach { it.setGravity(gravity) }
    }

    private fun getIndicatorViews() =
        mutableListOf(binding.indicatorView1, binding.indicatorView2, binding.indicatorView3)

    class NumberAdapter(dataList: List<Int>) :
        BaseAdapter<Int, NumberAdapterBinding>(dataList, R.layout.number_adapter) {

        override fun NumberAdapterBinding.onBindViewHolder(item: Int, position: Int) {
            tvNumber.text = position.toString()
            tvNumber.setBackgroundColor(Color.parseColor(if (position % 2 == 0) "#E37E7E" else "#E49542"))
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_primary_minus -> {
                val dp = binding.etPrimarySize.text.toString().toInt() - 1
                binding.etPrimarySize.setText(dp.coerceAtLeast(0).toString())
            }

            R.id.iv_primary_plus -> {
                val dp = binding.etPrimarySize.text.toString().toInt() + 1
                binding.etPrimarySize.setText(dp.toString())
            }

            R.id.iv_primary_checked_minus -> {
                val dp = binding.etPrimaryCheckedSize.text.toString().toInt() - 1
                binding.etPrimaryCheckedSize.setText(dp.coerceAtLeast(0).toString())
            }

            R.id.iv_primary_checked_plus -> {
                val dp = binding.etPrimaryCheckedSize.text.toString().toInt() + 1
                binding.etPrimaryCheckedSize.setText(dp.toString())
            }

            R.id.iv_secondary_minus -> {
                val dp = binding.etSecondarySize.text.toString().toInt() - 1
                binding.etSecondarySize.setText(dp.coerceAtLeast(0).toString())
            }

            R.id.iv_secondary_plus -> {
                val dp = binding.etSecondarySize.text.toString().toInt() + 1
                binding.etSecondarySize.setText(dp.toString())
            }

            R.id.iv_secondary_checked_minus -> {
                val dp = binding.etSecondaryCheckedSize.text.toString().toInt() - 1
                binding.etSecondaryCheckedSize.setText(dp.coerceAtLeast(0).toString())
            }

            R.id.iv_secondary_checked_plus -> {
                val dp = binding.etSecondaryCheckedSize.text.toString().toInt() + 1
                binding.etSecondaryCheckedSize.setText(dp.toString())
            }

            R.id.iv_gap_minus -> {
                val dp = binding.etGapSize.text.toString().toInt() - 1
                binding.etGapSize.setText(dp.toString())
            }

            R.id.iv_gap_plus -> {
                val dp = binding.etGapSize.text.toString().toInt() + 1
                binding.etGapSize.setText(dp.toString())
            }

            R.id.iv_round_radius_minus -> {
                val dp = binding.etRoundRadiusSize.text.toString().toInt() - 1
                binding.etRoundRadiusSize.setText(dp.coerceAtLeast(0).toString())
            }

            R.id.iv_round_radius_plus -> {
                val dp = binding.etRoundRadiusSize.text.toString().toInt() + 1
                binding.etRoundRadiusSize.setText(dp.toString())
            }
        }
    }

    private val Int.dp: Float
        get() {
            val scale = Resources.getSystem().displayMetrics.density
            return this * scale
        }

    private fun Editable?.getInt(mini: Int): Int {
        return toString().toIntOrNull()?.coerceAtLeast(mini) ?: mini
    }
}
