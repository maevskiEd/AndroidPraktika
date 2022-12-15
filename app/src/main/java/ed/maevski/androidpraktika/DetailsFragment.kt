package ed.maevski.androidpraktika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pic = arguments?.get("dev") as DeviantPicture

        //Устанавливаем заголовок
        binding.detailsToolbar.title = pic.title
        //Устанавливаем картинку
        binding.detailsPoster.setImageResource(pic.picture)
        //Устанавливаем описание
        binding.detailsDescription.text = pic.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}