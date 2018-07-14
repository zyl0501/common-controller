package com.tomtaw.lib_badge.view;


import com.tomtaw.lib_badge.BadgeHelper;
import com.tomtaw.lib_badge.BadgeNumber;

/**
 * Created by zyl on 2016/8/15.
 */
public class BadgeListenerWrap implements BadgeHelper.OnChangeListener {
  BadgeLayout badgeView;

  public BadgeListenerWrap(BadgeLayout badgeView) {
    this.badgeView = badgeView;
  }

  @Override
  public void onInit(BadgeNumber badge) {
    if (badge == null) {
      badgeView.setCount(0);
      badgeView.showBadge(false);
      return;
    }
    badgeView.setCount(badge.getCount());
    badgeView.setMode(badge.getDisplayMode() == BadgeNumber.BadgeMode.NUMBER);
    badgeView.showBadge(badge.getCount() > 0);
  }

  @Override
  public void onChange(BadgeNumber badge) {
    if (badge == null) {
      badgeView.setCount(0);
      badgeView.showBadge(false);
      return;
    }
    badgeView.setCount(badge.getCount());
    badgeView.setMode(badge.getDisplayMode() == BadgeNumber.BadgeMode.NUMBER);
    badgeView.showBadge(badge.getCount() > 0);
  }

  @Override
  public void onDisplay(BadgeNumber badge, boolean show) {
    badgeView.showBadge(show);
  }
}
