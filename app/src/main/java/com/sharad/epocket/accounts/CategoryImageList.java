package com.sharad.epocket.accounts;

import com.sharad.epocket.R;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class CategoryImageList {
    private static final int imageResource[] = {
    /*000*/ R.drawable.c_home_black_24dp,
    /*001*/ R.drawable.c_account_balance_wallet_black_24dp,
    /*002*/ R.drawable.c_favorite_black_24dp,
    /*003*/ R.drawable.c_insert_emoticon_black_24dp,
    /*004*/ R.drawable.c_language_black_24dp,
    /*005*/ R.drawable.c_lightbulb_outline_black_24dp,
    /*006*/ R.drawable.c_water_drop_black_24px,
    /*007*/ R.drawable.c_flash_on_black_24dp,
    /*008*/ R.drawable.c_local_laundry_service_black_24dp,
    /*009*/ R.drawable.c_kitchen_black_24px,
    /*010*/ R.drawable.c_build_black_24dp,
    /*011*/ R.drawable.c_live_tv_black_24dp,
    /*012*/ R.drawable.c_important_devices_black_24dp,
    /*013*/ R.drawable.c_local_activity_black_24dp,
    /*014*/ R.drawable.c_local_bar_black_24dp,
    /*015*/ R.drawable.c_local_cafe_black_24dp,
    /*016*/ R.drawable.c_smoking_rooms_black_24px,
    /*017*/ R.drawable.c_local_dining_black_24dp,
    /*018*/ R.drawable.c_local_pizza_black_24dp,
    /*019*/ R.drawable.c_cake_black_24dp,
    /*020*/ R.drawable.c_card_giftcard_black_24dp,
    /*021*/ R.drawable.c_local_florist_black_24dp,
    /*022*/ R.drawable.c_local_grocery_store_black_24dp,
    /*023*/ R.drawable.c_receipt_black_24dp,
    /*024*/ R.drawable.c_shop_black_24dp,
    /*025*/ R.drawable.c_shopping_basket_black_24dp,
    /*026*/ R.drawable.c_local_hospital_black_24dp,
    /*027*/ R.drawable.c_medicine_black_24px,
    /*028*/ R.drawable.c_pool_black_24px,
    /*029*/ R.drawable.c_fitness_center_black_24px,
    /*030*/ R.drawable.c_golf_course_black_24px,
    /*031*/ R.drawable.c_beach_access_black_24px,
    /*032*/ R.drawable.c_hotel_black_24dp,
    /*033*/ R.drawable.c_local_phone_black_24dp,
    /*034*/ R.drawable.c_local_gas_station_black_24dp,
    /*035*/ R.drawable.c_airplanemode_active_black_24dp,
    /*036*/ R.drawable.c_directions_boat_black_24dp,
    /*037*/ R.drawable.c_directions_bus_black_24dp,
    /*038*/ R.drawable.c_directions_car_black_24dp,
    /*039*/ R.drawable.c_local_shipping_black_24dp,
    /*040*/ R.drawable.c_local_taxi_black_24dp,
    /*041*/ R.drawable.c_motorcycle_black_24dp,
    /*042*/ R.drawable.c_directions_bike_black_24dp,
    /*043*/ R.drawable.c_train_black_24px,
    /*044*/ R.drawable.c_local_parking_black_24dp,
    /*045*/ R.drawable.c_traffic_black_24dp,
    /*046*/ R.drawable.c_casino_black_24px,
    /*047*/ R.drawable.c_movie_black_24dp,
    /*048*/ R.drawable.c_music_note_black_24dp,
    /*049*/ R.drawable.c_headset_black_24dp,
    /*050*/ R.drawable.c_network_wifi_black_24dp,
    /*051*/ R.drawable.c_palette_black_24dp,
    /*052*/ R.drawable.c_payment_black_24dp,
    /*053*/ R.drawable.c_notifications_black_24dp,
    /*054*/ R.drawable.c_person_black_24dp,
    /*055*/ R.drawable.c_group_black_24dp,
    /*056*/ R.drawable.c_pregnant_woman_black_24dp,
    /*057*/ R.drawable.c_child_friendly_black_24px,
    /*058*/ R.drawable.c_pets_black_24dp,
    /*059*/ R.drawable.c_phone_android_black_24dp,
    /*060*/ R.drawable.c_photo_camera_black_24dp,
    /*061*/ R.drawable.c_style_black_24dp,
    /*062*/ R.drawable.c_content_cut_black_24dp,
    /*063*/ R.drawable.c_weekend_black_24dp,
    /*064*/ R.drawable.c_hot_tub_black_24px,
    /*065*/ R.drawable.c_spa_black_24px,
    /*066*/ R.drawable.c_thumb_up_black_24dp,
    /*067*/ R.drawable.c_thumb_down_black_24dp,
    /*068*/ R.drawable.c_school_black_24dp,
    /*069*/ R.drawable.c_trending_up_black_24dp,
    /*070*/ R.drawable.c_trending_flat_black_24dp,
    /*071*/ R.drawable.c_trending_down_black_24dp,
    /*072*/ R.drawable.c_face_black_24dp,
    /*073*/ R.drawable.c_security_black_24dp,
    /*074*/ R.drawable.c_videogame_asset_black_24px,
    /*075*/ R.drawable.c_delete_black_24dp,
    /*076*/ R.drawable.c_account_balance_black_24dp,
    /*077*/ R.drawable.c_extension_black_24dp,
    /*078*/ R.drawable.c_camera_roll_black_24dp,
    /*079*/ R.drawable.c_widgets_black_24dp,
    /*080*/ R.drawable.c_local_mall_black_24dp,
    /*081*/ R.drawable.c_local_offer_black_24dp,
    /*082*/ R.drawable.c_tram_black_24px,
    /*083*/ R.drawable.c_assignment_ind_black_24dp,
    /*084*/ R.drawable.c_store_mall_directory_black_24dp,
    /*085*/ R.drawable.c_class_black_24dp,
    /*086*/ R.drawable.c_interest_black_24px,
    /*087*/ R.drawable.c_house_rent_black_24px,
            R.drawable.ic_help_outline_black_24dp,
            R.drawable.c_filter_tilt_shift_black_24px,
    };

    public static final int RESOURCE_COUNT = imageResource.length - 2;
    public static final int RESOURCE_UNKNOWN = imageResource.length - 2;
    public static final int RESOURCE_ADD_NEW = imageResource.length - 1;

    public static final int getImageResource(int index) {
        if(index < imageResource.length) {
            return imageResource[index];
        }
        return  0;
    }
}
