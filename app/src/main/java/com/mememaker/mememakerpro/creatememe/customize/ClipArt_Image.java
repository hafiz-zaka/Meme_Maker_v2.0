package com.mememaker.mememakerpro.creatememe.customize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mememaker.mememakerpro.creatememe.R;


public class ClipArt_Image extends RelativeLayout {

	int baseh;
	int basew;
	int basex;
	int basey;



	static ClipArt_Image selected;// static obj shared all classes obj


	ImageButton btndel;
	ImageButton btnrot;
	ImageButton btnscl;
	ImageButton btnDone;
	RelativeLayout clip;
	Context cntx;
	boolean freeze = false;
	int h;
	static int  i;

	static ImageView imageView;
	String imageUri;
	ImageView imgring;
	boolean isShadow;
	int iv;
	RelativeLayout layBg;
	RelativeLayout layGroup;
	LayoutParams layoutParams;
	public LayoutInflater mInflater;
	int margl;
	int margt;
	// DisplayImageOptions op;
	float opacity = 1.0F;
	Bitmap originalBitmap;
	int pivx;
	int pivy;
	int pos;
	Bitmap shadowBitmap;
	float startDegree;
	String[] v;


	static int ii;


	public ClipArt_Image(Context paramContext) {
		super(paramContext);
		cntx = paramContext;
		layGroup = this;
		basex = 0;
		basey = 0;
		pivx = 0;
		pivy = 0;
		mInflater = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mInflater.inflate(R.layout.clipart_image, this, true);
		btndel = ((ImageButton) findViewById(R.id.del));
		btnrot = ((ImageButton) findViewById(R.id.rotate));
		btnscl = ((ImageButton) findViewById(R.id.sacle));
		imgring = ((ImageView) findViewById(R.id.image));
		btnDone = findViewById(R.id.done);
		layoutParams = new LayoutParams(250,250);
		layGroup.setLayoutParams(layoutParams);
		 imageView=findViewById(R.id.clipart_img);
		setOnTouchListener(new OnTouchListener() {
			final GestureDetector gestureDetector = new GestureDetector(ClipArt_Image.this.cntx,
					new GestureDetector.SimpleOnGestureListener() {
						public boolean onDoubleTap(MotionEvent paramAnonymous2MotionEvent) {
							return false;
						}
					});

			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				ClipArt_Image.this.visiball();
				if (!ClipArt_Image.this.freeze) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							layGroup.invalidate();
							gestureDetector.onTouchEvent(event);

							layGroup.bringToFront();

							layGroup.performClick();
							basex = ((int) (event.getRawX() - layoutParams.leftMargin));
							basey = ((int) (event.getRawY() - layoutParams.topMargin));
							break;
						case MotionEvent.ACTION_MOVE:
							int i = (int) event.getRawX();
							int j = (int) event.getRawY();
							layBg = (RelativeLayout) (getParent());
							if ((i - basex > -(layGroup.getWidth() * 2 / 3))
									&& (i - basex < layBg.getWidth() - layGroup.getWidth() / 3)) {
								layoutParams.leftMargin = (i - basex);
							}
							if ((j - basey > -(layGroup.getHeight() * 2 / 3))
									&& (j - basey < layBg.getHeight() - layGroup.getHeight() / 3)) {
								layoutParams.topMargin = (j - basey);
							}
							layoutParams.rightMargin = -9999999;
							layoutParams.bottomMargin = -9999999;
							layGroup.setLayoutParams(layoutParams);
							break;

					}

					return true;
				}
				return true;
				// freeze;
			}
		});

		btnscl.setOnTouchListener(new OnTouchListener() {
			@SuppressLint({ "NewApi" })
			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				if (!ClipArt_Image.this.freeze) {
					int j = (int) event.getRawX();
					int i = (int) event.getRawY();
					layoutParams = (LayoutParams) layGroup.getLayoutParams();
					switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							ClipArt_Image.this.layGroup.invalidate();
							ClipArt_Image.this.basex = j;
							ClipArt_Image.this.basey = i;
							ClipArt_Image.this.basew = ClipArt_Image.this.layGroup.getWidth();
							ClipArt_Image.this.baseh = ClipArt_Image.this.layGroup.getHeight();
							int[] loaction = new int[2];
							layGroup.getLocationOnScreen(loaction);
							margl = layoutParams.leftMargin;
							margt = layoutParams.topMargin;
							break;
						case MotionEvent.ACTION_MOVE:

							float f2 = (float) Math.toDegrees(Math.atan2(i - ClipArt_Image.this.basey, j - ClipArt_Image.this.basex));
							float f1 = f2;
							if (f2 < 0.0F) {
								f1 = f2 + 360.0F;
							}
							j -= ClipArt_Image.this.basex;
							int k = i - ClipArt_Image.this.basey;
							i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
									- ClipArt_Image.this.layGroup.getRotation())));
							j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
									- ClipArt_Image.this.layGroup.getRotation())));
							k = i * 2 + ClipArt_Image.this.basew;
							int m = j * 2 + ClipArt_Image.this.baseh;
							if (k > 150) {
								layoutParams.width = k;
								layoutParams.leftMargin = (ClipArt_Image.this.margl - i);
							}
							if (m > 150) {
								layoutParams.height = m;
								layoutParams.topMargin = (ClipArt_Image.this.margt - j);
							}
							ClipArt_Image.this.layGroup.setLayoutParams(layoutParams);
							ClipArt_Image.this.layGroup.performLongClick();
							break;
					}
					return true;

				}
				return ClipArt_Image.this.freeze;
			}
		});
		btnrot.setOnTouchListener(new OnTouchListener() {
			@SuppressLint({ "NewApi" })
			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				if (!ClipArt_Image.this.freeze) {
					layoutParams = (LayoutParams) ClipArt_Image.this.layGroup.getLayoutParams();
					ClipArt_Image.this.layBg = ((RelativeLayout) ClipArt_Image.this.getParent());
					int[] arrayOfInt = new int[2];
					layBg.getLocationOnScreen(arrayOfInt);
					int i = (int) event.getRawX() - arrayOfInt[0];
					int j = (int) event.getRawY() - arrayOfInt[1];
					switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							ClipArt_Image.this.layGroup.invalidate();
							ClipArt_Image.this.startDegree = layGroup.getRotation();
							ClipArt_Image.this.pivx = (layoutParams.leftMargin + ClipArt_Image.this.getWidth() / 2);
							ClipArt_Image.this.pivy = (layoutParams.topMargin + ClipArt_Image.this.getHeight() / 2);
							ClipArt_Image.this.basex = (i - ClipArt_Image.this.pivx);
							ClipArt_Image.this.basey = (ClipArt_Image.this.pivy - j);
							break;

						case MotionEvent.ACTION_MOVE:
							int k = ClipArt_Image.this.pivx;
							int m = ClipArt_Image.this.pivy;
							j = (int) (Math.toDegrees(Math.atan2(ClipArt_Image.this.basey, ClipArt_Image.this.basex)) - Math
									.toDegrees(Math.atan2(m - j, i - k)));
							i = j;
							if (j < 0) {
								i = j + 360;
							}
							ClipArt_Image.this.layGroup.setRotation((ClipArt_Image.this.startDegree + i) % 360.0F);
							break;
					}

					return true;
				}
				return ClipArt_Image.this.freeze;
			}
		});
		this.btndel.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				if (!ClipArt_Image.this.freeze) {
					layBg = ((RelativeLayout) ClipArt_Image.this.getParent());
					layBg.performClick();
					layBg.removeView(ClipArt_Image.this.layGroup);
				}
			}
		});

		btnDone.setOnClickListener(view -> {
			disableAll();
		});

	}

	public void disableAll() {
		btndel.setVisibility(View.INVISIBLE);
		btnrot.setVisibility(View.INVISIBLE);
		btnscl.setVisibility(View.INVISIBLE);
		imgring.setVisibility(View.INVISIBLE);
		btnDone.setVisibility(View.INVISIBLE);
	}





	public void resetImage() {
		this.originalBitmap = null;
		this.layGroup.performLongClick();
	}

//	public static ImageView setNewImg(int i) {
//		ClipArt_Image.imageView.setImageResource(i);
//		return imageView;
//	}
//	public static ImageView getNewImg() {
//		return ClipArt_Image.imageView;
//	}

	public void setFreeze(boolean paramBoolean) {
		this.freeze = paramBoolean;
	}


	public void setLocation() {
		this.layBg = ((RelativeLayout) getParent());
		RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) this.layGroup.getLayoutParams();
		localLayoutParams.topMargin = ((int) (Math.random() * (this.layBg.getHeight() - 400)));
		localLayoutParams.leftMargin = ((int) (Math.random() * (this.layBg.getWidth() - 400)));
		this.layGroup.setLayoutParams(localLayoutParams);
	}

	public void visiball() {
		btndel.setVisibility(View.VISIBLE);
		btnrot.setVisibility(View.VISIBLE);
		btnscl.setVisibility(View.VISIBLE);
		imgring.setVisibility(View.VISIBLE);
		btnDone.setVisibility(View.VISIBLE);
	}




	public static abstract interface DoubleTapListener {
		public abstract void onDoubleTap();
	}

	public static ClipArt_Image getSelected() {
		return selected;
	}

	public static void setSelected(ClipArt_Image select) {
		selected = select;
	}

	public static ImageView getImageView(int i) {
		imageView.setImageResource(i);
		return imageView;
	}

	public static int setImageView(int i){
		return i;
	}

}

