package com.bwzz.exnew.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bwzz.common.utils.L;
import com.bwzz.exnew.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BaseRepeatedPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * @author wanghb
 * @date 15/4/7.
 */
public class FacebookFresco extends BaseFragment {

  @InjectView(R.id.drawee)
  SimpleDraweeView simpleDraweeView;

  private GenericDraweeHierarchy hierarchy;
  private static final Uri uri1 = Uri.parse("http://www.xxbt.com/attachments/dahai/1301665376_237.jpg");
  private static final Uri uri2 = Uri.parse("http://www.xxbt.com/attachments/dahai/1301665509_971.jpg");

  private Uri[] uris = new Uri[]{uri1, uri2,
      Uri.parse("res://drawable/" + R.drawable.muyang),
//      Uri.parse("asset://asset/anim.gif"),
      Uri.parse("http://ww1.sinaimg.cn/mw600/6345d84ejw1dvxp9dioykg.gif")};

  @Override
  protected int getLayoutResId() {
    return R.layout.fresco;
  }

  @Override
  protected void setupView(LayoutInflater inflater, View view, Bundle savedInstanceState) {
    super.setupView(inflater, view, savedInstanceState);
    ButterKnife.inject(this, view);
    setupWithUri(uris[3]);
  }

  @OnClick(R.id.drawee)
  public void random(View view) {
    Random random = new Random();
    Uri uri = uris[random.nextInt(uris.length)];
    setupWithUri(uri);
  }

  private void setupWithUri(Uri uri) {
    L.e("set uri : ", uri.toString());
    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
      @Override
      public void onFinalImageSet(
          String id,
          @Nullable ImageInfo imageInfo,
          @Nullable final Animatable anim) {
        if (imageInfo == null) {
          return;
        }
        QualityInfo qualityInfo = imageInfo.getQualityInfo();
        L.e(id,
            imageInfo.getWidth(),
            imageInfo.getHeight(),
            qualityInfo.getQuality(),
            qualityInfo.isOfGoodEnoughQuality(),
            qualityInfo.isOfFullQuality());

//        simpleDraweeView.postDelayed(new Runnable() {
//          @Override
//          public void run() {
//            if (anim != null) {
//              anim.start();
//            }
//          }
//        }, 20);
      }

      @Override
      public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
        L.e(getClass(), "Intermediate image received");
      }

      @Override
      public void onFailure(String id, Throwable throwable) {
        L.e(getClass(), throwable, "Error loading %s", id);
      }
    };

    Postprocessor myPostprocessor = new Postprocessor() {
      @Override
      public void process(Bitmap bitmap) {
        L.e("post process");
        for (int x = 0; x < bitmap.getWidth(); x += 2) {
          for (int y = 0; y < bitmap.getHeight(); y += 2) {
            bitmap.setPixel(x, y, Color.RED);
          }
        }
      }

      @Override
      public String getName() {
        return "Postprocessor";
      }
    };

    ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
        .setBackgroundColor(Color.GREEN)
        .build();

    ImageRequest request = ImageRequestBuilder
        .newBuilderWithSource(uri)
        .setAutoRotateEnabled(true)
        .setLocalThumbnailPreviewsEnabled(true)
        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
        .setProgressiveRenderingEnabled(false)
        .setResizeOptions(new ResizeOptions(300, 600))//?
        .setPostprocessor(meshPostprocessor)
        .setImageDecodeOptions(decodeOptions)
        .setProgressiveRenderingEnabled(true)
        .build();

    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setControllerListener(controllerListener)
            // 预期不大一致呀
//        .setLowResImageRequest(ImageRequest.fromUri("http://www.yooyoo360.com/photo/2009-1-1/20090112134150576.jpg"))
        .setImageRequest(request)
        .setOldController(simpleDraweeView.getController())
        .setAutoPlayAnimations(true)
//        .setUri(uri)
        .build();

    simpleDraweeView.setController(controller);

    if (hierarchy == null) {

      RoundingParams param = new RoundingParams();
      param.setBorder(Color.LTGRAY, 10);
      param.setCornersRadii(200, 30, 400, 50);
      param.setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
//      param.setRoundAsCircle(true);
      param.setOverlayColor(Color.RED);
      List<Drawable> backgroundsList = new ArrayList<>();
      List<Drawable> overlaysList = new ArrayList<>();
      GenericDraweeHierarchyBuilder builder =
          new GenericDraweeHierarchyBuilder(getResources());
      hierarchy = builder
          .setFadeDuration(300)
          .setPlaceholderImage(getResources().getDrawable(R.mipmap.ic_launcher))
          .setBackgrounds(backgroundsList)
          .setOverlays(overlaysList)
          .setRoundingParams(param)
          .build();

      // 不要重复设置Hierarchy
      simpleDraweeView.setHierarchy(hierarchy);
    }

    hierarchy.setPlaceholderImage(R.mipmap.ic_launcher);
    hierarchy.setActualImageFocusPoint(new PointF(0.45f, 0.5f));
    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
    // called by controller
//    hierarchy.setProgress(60, true);
//    hierarchy.setImage(getResources().getDrawable(R.mipmap.ic_launcher), false, 60);

    RoundingParams param = hierarchy.getRoundingParams();
    param.setBorder(Color.BLACK, 25f);
    param.setOverlayColor(Color.WHITE);
    param.setRoundAsCircle(new Random().nextBoolean());

  }

  public class MeshPostprocessor extends BaseRepeatedPostProcessor {
    private int mColor = Color.RED;

    public void setColor(int color) {
      mColor = color;
      update();
    }

    @Override
    public String getName() {
      return "meshPostprocessor";
    }

    @Override
    public void process(Bitmap bitmap) {
      for (int x = 0; x < bitmap.getWidth(); x += 10) {
        for (int y = 0; y < bitmap.getHeight(); y += 5) {
          bitmap.setPixel(x, y, mColor);
        }
      }
    }
  }

  MeshPostprocessor meshPostprocessor = new MeshPostprocessor();

}
