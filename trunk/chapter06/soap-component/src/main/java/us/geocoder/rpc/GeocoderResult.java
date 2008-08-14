package us.geocoder.rpc;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author David Dossot (david@dossot.net)
 */
public class GeocoderResult {
    private String city;

    private String state;

    private String zip;

    private float lat;

    private float longitude;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(final float lat) {
        this.lat = lat;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(final float longitude) {
        this.longitude = longitude;
    }
}
