package com.sample.wifip2psample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import com.sample.wifip2psample.MainActivity


class WiFiDirectBroadcastReceiver(val manager: WifiP2pManager, val channel: WifiP2pManager.Channel,
    val myPeerListListener: PeerListListener, val connectionListener: WifiP2pManager.ConnectionInfoListener, val activity: MainActivity) : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val action = intent.action
    when (action) {
      WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
        // Check to see if Wi-Fi is enabled and notify appropriate activity
        val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
          // Wifi P2P is enabled
        } else {
          // Wi-Fi P2P is not enabled
        }
      }
      WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
        // Call WifiP2pManager.requestPeers() to get a list of current peers
        manager.requestPeers(channel, myPeerListListener);
      }
      WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
        // Respond to new connection or disconnections
        val networkInfo = intent.getParcelableExtra(
            WifiP2pManager.EXTRA_NETWORK_INFO) as NetworkInfo;
        if (networkInfo.isConnected()) {
          // We are connected with the other device, request connection
          // info to find group owner IP
          manager.requestConnectionInfo(channel, connectionListener)
        }
      }
      WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
        // Respond to this device's wifi state changing
//        val fragment = activity.fragmentManager
//            .findFragmentById(R.id.frag_list) as DeviceListFragment
//        fragment.updateThisDevice(intent.getParcelableExtra(
//            WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice)
      }
    }
  }
}
