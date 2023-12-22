package com.mememaker.mememakerpro.creatememe.customize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mememaker.mememakerpro.creatememe.R;


public class ClipArt extends RelativeLayout {

	int baseh;
	int basew;
	int basex;
	int basey;

	static ClipArt selected;// static obj shared all classes obj


	ImageButton btndel;
	ImageButton btnrot;
	ImageButton btnscl;
	ImageButton btnDone;

	Context cntx;
	boolean freeze = false;

	TextView text;
	ImageView imgring;

	RelativeLayout layBg;
	RelativeLayout layGroup;
	RelativeLayout.LayoutParams layoutParams;
	public LayoutInflater mInflater;
	int margl;
	int margt;
	Bitmap originalBitmap;
	int pivx;
	int pivy;
	float startDegree;
	private static String setFont="Acme";


	public void setTextFont(String fontName)
	{
		text.setTag(fontName);
	}

	public Object getTextFont()
	{
		return text.getTag();
	}

	public ClipArt(Context paramContext) {
		super(paramContext);
		cntx = paramContext;
		layGroup = this;
		basex = 0;
		basey = 0;
		pivx = 0;
		pivy = 0;
		mInflater = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mInflater.inflate(R.layout.clipart, this, true);
		btndel = findViewById(R.id.del);
		btnrot = findViewById(R.id.rotate);
		btnscl = findViewById(R.id.sacle);
		btnDone = findViewById(R.id.done);
		imgring = findViewById(R.id.image);
		layoutParams = new LayoutParams(350, 200);
		layGroup.setLayoutParams(layoutParams);
		text = findViewById(R.id.clipart);
//		text.setText("Welcome");
		text.setHint("Tab here to add text");
		text.setHintTextColor(getResources().getColor(R.color.black));
		text.setTag(setFont);
		setOnTouchListener(new OnTouchListener() {
			final GestureDetector gestureDetector = new GestureDetector(ClipArt.this.cntx,
					new GestureDetector.SimpleOnGestureListener() {
						public boolean onDoubleTap(MotionEvent paramAnonymous2MotionEvent) {
							return false;
						}
					});

			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				ClipArt.this.visiball();
				if (!ClipArt.this.freeze) {
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

		this.btnscl.setOnTouchListener(new OnTouchListener() {
			@SuppressLint({ "NewApi" })
			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				if (!freeze) {
					int j = (int) event.getRawX();
					int i = (int) event.getRawY();
					layoutParams = (LayoutParams) layGroup.getLayoutParams();
					switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							layGroup.invalidate();
							basex = j;
							basey = i;
							basew = layGroup.getWidth();
							baseh = layGroup.getHeight();
							int[] loaction = new int[2];
							layGroup.getLocationOnScreen(loaction);
							margl = layoutParams.leftMargin;
							margt = layoutParams.topMargin;
							break;
						case MotionEvent.ACTION_MOVE:

							float f2 = (float) Math.toDegrees(Math.atan2(i - basey, j - basex));
							float f1 = f2;
							if (f2 < 0.0F) {
								f1 = f2 + 360.0F;
							}
							j -= ClipArt.this.basex;
							int k = i - ClipArt.this.basey;
							i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
									- ClipArt.this.layGroup.getRotation())));
							j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
									- ClipArt.this.layGroup.getRotation())));
							k = i * 2 + ClipArt.this.basew;
							int m = j * 2 + ClipArt.this.baseh;
							if (k > 150) {
								layoutParams.width = k;
								layoutParams.leftMargin = (margl - i);
							}
							if (m > 150) {
								layoutParams.height = m;
								layoutParams.topMargin = (margt - j);
							}
							layGroup.setLayoutParams(layoutParams);
							layGroup.performLongClick();
							break;
					}
					return true;

				}
				return ClipArt.this.freeze;
			}
		});
		btnrot.setOnTouchListener(new OnTouchListener() {
			@SuppressLint({ "NewApi" })
			public boolean onTouch(View paramAnonymousView, MotionEvent event) {
				if (!ClipArt.this.freeze) {
					layoutParams = (LayoutParams) layGroup.getLayoutParams();
					layBg = ((RelativeLayout) getParent());
					int[] arrayOfInt = new int[2];
					layBg.getLocationOnScreen(arrayOfInt);
					int i = (int) event.getRawX() - arrayOfInt[0];
					int j = (int) event.getRawY() - arrayOfInt[1];
					switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							layGroup.invalidate();
							startDegree = layGroup.getRotation();
							pivx = (layoutParams.leftMargin + getWidth() / 2);
							pivy = (layoutParams.topMargin + getHeight() / 2);
							basex = (i - pivx);
							basey = (pivy - j);
							break;

						case MotionEvent.ACTION_MOVE:
							int k = pivx;
							int m = pivy;
							j = (int) (Math.toDegrees(Math.atan2(basey, basex)) - Math
									.toDegrees(Math.atan2(m - j, i - k)));
							i = j;
							if (j < 0) {
								i = j + 360;
							}
							layGroup.setRotation((startDegree + i) % 360.0F);
							break;
					}

					return true;
				}
				return freeze;
			}
		});
		btndel.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				if (!freeze) {
					layBg = ((RelativeLayout) getParent());
					layBg.performClick();
					layBg.removeView(layGroup);
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

	public TextView getTextView() {
		return this.text;
	}
	public void setText(String s){
		text.setText(s);
	}


	public void resetImage() {
		this.originalBitmap = null;
		this.layGroup.performLongClick();
	}

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

	public static ClipArt getSelectedText(){
		return selected;
	}
	public static void setSelectedText(ClipArt tv){
		selected = tv;
	}


	public static abstract interface DoubleTapListener {
		public abstract void onDoubleTap();
	}
}

