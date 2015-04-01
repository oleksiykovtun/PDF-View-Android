package com.artifex.mupdfdemo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.artifex.mupdfdemo.LinkInfoExternal;
import com.artifex.mupdfdemo.MuPDFPageView;
import com.artifex.mupdfdemo.PageView;
import com.artifex.mupdfdemo.domain.SearchTaskResult;

public abstract class DocumentReaderView extends ReaderView {
	private static final String TAG = "DocumentReaderView";

	private enum LinkState {
		DEFAULT, HIGHLIGHT, INHIBIT
	};

	private static int tapPageMargin = 70;

	private LinkState linkState = LinkState.DEFAULT;

	private boolean showButtonsDisabled;

	public DocumentReaderView(Context context, 
			SparseArray<LinkInfoExternal[]> pLinkOfDocument) {
		super(context, pLinkOfDocument);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		tapPageMargin = (int) (getWidth() * .1);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (!showButtonsDisabled) {
			int linkPage = -1;
			String linkString = null;
			if (linkState != LinkState.INHIBIT) {
				MuPDFPageView pageView = (MuPDFPageView) getDisplayedView();
				if (pageView != null) {
					linkPage = pageView.hitLinkPage(e.getX(), e.getY());
					linkString = pageView.hitLinkUri(e.getX(),  e.getY());
				}
			}

			if (linkPage != -1) {
				// block pageView from sliding to next page
				noAutomaticSlide = true;
				Log.d(TAG,"linkPage ="+ linkPage);
				setDisplayedViewIndex(linkPage);
			} else if (linkString != null) {
				// start intent with url as linkString
				openLink(linkString);
			} else {
				if (e.getX() < tapPageMargin) {
						Log.d(TAG, "moveToPrevious");
						super.moveToPrevious();
					} else if (e.getX() > super.getWidth() - tapPageMargin) {
						Log.d(TAG, "moveToNext");
						super.moveToNext();
					} else {
						onContextMenuClick();
					}
			}
		}
		return super.onSingleTapUp(e);
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector d) {
		// Disabled showing the buttons until next touch.
		// Not sure why this is needed, but without it
		// pinch zoom can make the buttons appear
		showButtonsDisabled = true;
		return super.onScaleBegin(d);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			showButtonsDisabled = false;
		}
		return super.onTouchEvent(event);
	}

	public boolean isShowButtonsDisabled() {
		return showButtonsDisabled;
	}

	abstract protected void onContextMenuClick();
	abstract protected void onBuy(String path);

	//	protected void onChildSetup(int i, View v) {
//		if (SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber == i)
//			((PageView)v).setSearchBoxes(SearchTaskResult.get().searchBoxes);
//		else
//			((PageView)v).setSearchBoxes(null);
//
//		((PageView)v).setLinkHighlighting(mLinkState == LinkState.HIGHLIGHT);
//	}

	@Override
	protected void onMoveToChild(View view, int i) {

//		mPageNumberView.setText(String.format("%d/%d", i+1, core.countPages()));
//		mPageSlider.setMax((core.countPages()-1) * mPageSliderRes);
//		mPageSlider.setProgress(i * mPageSliderRes);
		if (SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber != i) {
			SearchTaskResult.recycle();
			resetupChildren();
		}
	}

	@Override
	protected void onSettle(View v) {
		((PageView)v).addHq(true);
	}

	@Override
	protected void onUnsettle(View v) {
		((PageView)v).removeHq();
	}

	@Override
	protected void onNotInUse(View v) {
		((PageView)v).releaseResources();
	}

	/**
	 * @param linkString - url to open
	 */
	private void openLink(String linkString) {
		Log.d(TAG, "!openLink " + linkString);
		Uri uri = Uri.parse(linkString);
		String warect = uri.getQueryParameter("warect");
		if(linkString.startsWith("http://") || linkString.startsWith("http://")) {
            Uri videoUri = Uri.parse(linkString);
            Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
            getContext().startActivity(intent);
        }
	}
}
