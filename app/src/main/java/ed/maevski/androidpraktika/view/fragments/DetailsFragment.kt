package ed.maevski.androidpraktika.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ed.maevski.androidpraktika.R
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    lateinit var picture: DeviantPicture

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picture = arguments?.get("dev") as DeviantPicture

        //Устанавливаем сердечко
        binding.detailsFabFavorites.setImageResource(
            if (picture.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )

        //Устанавливаем заголовок
        binding.detailsToolbar.title = picture.title
        //Устанавливаем картинку
        Glide.with(this)
            .load(picture.url)
            .centerCrop()
            .into(binding.detailsPoster)

        //Устанавливаем описание
        binding.detailsDescription.text = picture.description

        binding.detailsFabFavorites.setOnClickListener {
            if (!picture.isInFavorites) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                picture.isInFavorites = true
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                picture.isInFavorites = false
            }
        }

        binding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this picture: ${picture.title} \n\n ${picture.author}\n\n ${picture.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}