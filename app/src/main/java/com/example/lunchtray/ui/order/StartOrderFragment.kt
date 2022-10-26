
package com.example.lunchtray.ui.order

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lunchtray.R
import com.example.lunchtray.databinding.FragmentStartOrderBinding

/**
 * [StartOrderFragment] allows people to click the start button to start an order.
 */
class StartOrderFragment : Fragment() {

    // Binding object instance corresponding to the fragment_start_order.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var _binding: FragmentStartOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("StartOrderFragment","onCreateView() đã chạy")
        _binding = FragmentStartOrderBinding.inflate(inflater, container, false)
        // Điều hướng đến entree menu
        return binding.root
    }

    /**
    * Phương thức vòng đời của phân đoạn này được gọi khi hệ thống phân cấp chế độ xem được liên kết với phân đoạn
    * đang bị xóa. Kết quả là, xóa đối tượng ràng buộc.
    */
    // Tạo lại
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("StartOrderFragment","onViewCreated() đã chạy")

        binding.startOrderBtn.setOnClickListener {
            // TODO: điều hướng đến EntreeMenuFragment
            findNavController().navigate(R.id.action_startOrderFragment_to_entreeMenuFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("StartOrderFragment","onDestroyView() đã chạy!")
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        Log.d("StartOrderFragment", "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("StartOrderFragment", "onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("StartOrderFragment", "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("StartOrderFragment", "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("StartOrderFragment", "onDestroy Called")
    }


}
