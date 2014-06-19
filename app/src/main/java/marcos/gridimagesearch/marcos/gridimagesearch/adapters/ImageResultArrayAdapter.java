package marcos.gridimagesearch.marcos.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import marcos.gridimagesearch.R;
import marcos.gridimagesearch.models.ImageResult;

/**
 * Created by marcos on 6/17/14.
 */
public class ImageResultArrayAdapter  extends ArrayAdapter<ImageResult>{

    public ImageResultArrayAdapter(Context context, List<ImageResult> list) {
        super(context, R.layout.item_image_result, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        SmartImageView ivImage;

            if(convertView == null){
                LayoutInflater inflator = LayoutInflater.from(getContext());
                ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result,parent,false);

            } else {
                ivImage = (SmartImageView)convertView;
                ivImage.setImageResource(android.R.color.transparent);
            }

        ivImage.setImageUrl(imageInfo.getThumbUrl());

        return ivImage;
    }
}
