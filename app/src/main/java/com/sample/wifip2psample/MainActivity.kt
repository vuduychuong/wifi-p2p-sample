package com.sample.wifip2psample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sample.wifip2psample.broadcast.WiFiDirectBroadcastReceiver


class MainActivity : AppCompatActivity(), WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

  lateinit var mManager: WifiP2pManager
  lateinit var mChannel: WifiP2pManager.Channel
  lateinit var mReceiver: BroadcastReceiver
  lateinit var mIntentFilter: IntentFilter
  lateinit var mConfig: WifiP2pConfig

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    println("MainActivity")
    fab.setOnClickListener { view ->
      connectPeer(mConfig)
    }
    fab2.setOnClickListener { view -> getDiscoverPeer() }

    fab3.setOnClickListener { view -> createGroup() }
    mManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    mChannel = mManager.initialize(this, mainLooper, null)
    mReceiver = WiFiDirectBroadcastReceiver(mManager, mChannel, this, this, this)
    mIntentFilter = IntentFilter()
    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    mConfig = WifiP2pConfig()
  }

  fun getDiscoverPeer() {
    mManager.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
      override fun onSuccess() {
        //...
        println("discover success")
      }

      override fun onFailure(reasonCode: Int) {
        //...
        println("discover failed")
      }
    })
  }

  fun connectPeer(config: WifiP2pConfig) {
    if (config.deviceAddress == null) return
    mManager.connect(mChannel, config, object : WifiP2pManager.ActionListener {
      override fun onSuccess() {
        println("connected!!!")
        println(config.deviceAddress)
      }

      override fun onFailure(reason: Int) {
        println("connect failed!!!")
      }
    })
  }

  fun createGroup() {
    mManager.createGroup(mChannel, object : WifiP2pManager.ActionListener {
      override fun onSuccess() {
        println("P2p group create success")
      }

      override fun onFailure(p0: Int) {
        println("P2p group create failed")
      }
    })
  }

  override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
    val deviceList = p0?.deviceList
    if (deviceList != null) {
      for (device in deviceList) {
        println(device.deviceAddress)
        println(device.deviceName)
        mConfig.deviceAddress = device.deviceAddress
        mConfig.wps.setup = WpsInfo.PBC;
      }
    }
  }

  override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
    val groupOwnerAddress = info?.groupOwnerAddress?.getHostAddress()
    if (info?.groupFormed!! and info.isGroupOwner) {

    } else if (info.groupFormed) {

    }
  }

  override fun onResume() {
    super.onResume()
    registerReceiver(mReceiver, mIntentFilter)
  }

  override fun onPause() {
    super.onPause()
    unregisterReceiver(mReceiver)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }
}
