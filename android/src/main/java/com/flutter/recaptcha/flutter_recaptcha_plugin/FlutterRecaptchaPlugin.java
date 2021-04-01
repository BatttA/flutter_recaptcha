package com.flutter.recaptcha.flutter_recaptcha_plugin;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterRecaptchaPlugin
 */
public class FlutterRecaptchaPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    private MethodChannel channel;

    // activity required to call SafetyNet API
    private Activity activity;

    public FlutterRecaptchaPlugin() {
    }

    private void initChannel(BinaryMessenger messenger) {
        channel = new MethodChannel(messenger, "flutter_recaptcha_plugin");
        channel.setMethodCallHandler(this);
    }
    private void initActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        initChannel(binding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        initActivity(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        initActivity(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    // static registerWith required to support older v1 Android Embeddings
    public static void registerWith(Registrar registrar) {
        final FlutterRecaptchaPlugin plugin = new FlutterRecaptchaPlugin();
        plugin.initChannel(registrar.messenger());
        plugin.initActivity(registrar.activity());
    }

    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("verify")) {
            getToken(call, result);
        } else {
            result.notImplemented();
        }
    }

    private void getToken(MethodCall call, final MethodChannel.Result result) {
        String apiKey = call.argument("key");
        SafetyNet.getClient(activity).verifyWithRecaptcha(apiKey)
                .addOnSuccessListener(
                     new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {

                                if (!response.getTokenResult().isEmpty()) {
                                    result.success(response.getTokenResult());
                                } else {
                                    result.error("Cannot get token",
                                            "Token is empty", null);
                                }
                            }
                        })
                .addOnFailureListener(
                         new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                result.error("Cannot get token",
                                        e.getMessage(), null
                                );
                            }
                        });
    }
}
