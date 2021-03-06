package au.gov.amsa.navigation;

import static com.google.common.base.Optional.of;
import static java.lang.Math.toRadians;

import java.util.concurrent.atomic.AtomicLong;

import au.gov.amsa.navigation.VesselPosition.Builder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class VesselPosition {
	
	public static enum NavigationalStatus {AT_ANCHOR, MOORED, UNKNOWN }

	private static final double EARTH_RADIUS_KM = 6378.1;
	private static final int maxDimensionMetresWhenUnknown = 30;
	private final double lat;
	private final double lon;
	private final Optional<Integer> lengthMetres;
	private final Optional<Integer> widthMetres;
	private final Optional<Double> cogDegrees;
	private final Optional<Double> headingDegrees;
	private final Optional<Double> speedMetresPerSecond;
	private Optional<String> positionAisNmea;
	private Optional<String> shipStaticAisNmea;
	private final NavigationalStatus navigationalStatus;
	private final long time;
	private final Identifier id;
	private final VesselClass cls;
	private final Optional<Integer> shipType;

	private static AtomicLong counter = new AtomicLong();
	private long messageId;

	private VesselPosition(long messageId, Identifier id, double lat,
			double lon, Optional<Integer> lengthMetres,
			Optional<Integer> widthMetres, Optional<Double> cog,
			Optional<Double> heading, Optional<Double> speedMetresPerSecond,
			VesselClass cls, NavigationalStatus navigationalStatus, long time,
			Optional<Integer> shipType, Optional<String> positionAisNmea,
			Optional<String> shipStaticAisNmea) {

		Preconditions.checkArgument(lat >= -90 && lat <= 90, "unexpected lat "
				+ lat);
		Preconditions.checkArgument(lon >= -180 && lon <= 180,
				"unexpected lon " + lon);
		Preconditions.checkNotNull(id);
		Preconditions.checkNotNull(lengthMetres);
		Preconditions.checkNotNull(widthMetres);
		Preconditions.checkNotNull(shipType);
		Preconditions.checkNotNull(positionAisNmea);
		Preconditions.checkNotNull(shipStaticAisNmea);
		Preconditions.checkNotNull(navigationalStatus);

		this.messageId = messageId;
		this.cls = cls;
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.lengthMetres = lengthMetres;
		this.widthMetres = widthMetres;
		this.cogDegrees = cog;
		this.headingDegrees = heading;
		this.speedMetresPerSecond = speedMetresPerSecond;
		this.time = time;
		this.navigationalStatus = navigationalStatus;
		this.shipType = shipType;
		this.positionAisNmea = positionAisNmea;
		this.shipStaticAisNmea = shipStaticAisNmea;
	}

	public long messageId() {
		return messageId;
	}

	public Identifier id() {
		return id;
	}

	public double lat() {
		return lat;
	}

	public double lon() {
		return lon;
	}

	public Optional<Integer> lengthMetres() {
		return lengthMetres;
	}

	public Optional<Integer> widthMetres() {
		return widthMetres;
	}

	public Optional<Integer> maxDimensionMetres() {
		if (lengthMetres.isPresent() && widthMetres.isPresent())
			return Optional.of(Math.max(lengthMetres.get(), widthMetres.get()));
		else
			return Optional.absent();
	}

	public Optional<Double> cogDegrees() {
		return cogDegrees;
	}

	public Optional<Double> headingDegrees() {
		return headingDegrees;
	}

	public Optional<Double> speedMetresPerSecond() {
		return speedMetresPerSecond;
	}

	public VesselClass cls() {
		return cls;
	}

	public long time() {
		return time;
	}

	public NavigationalStatus navigationalStatus() {
		return navigationalStatus;
	}

	public Optional<Integer> shipType() {
		return shipType;
	}

	public Optional<String> positionAisNmea() {
		return positionAisNmea;
	}

	public Optional<String> shipStaticAisNmea() {
		return shipStaticAisNmea;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Identifier id;
		private double lat;
		private double lon;
		private Optional<Integer> lengthMetres = Optional.absent();
		private Optional<Integer> widthMetres = Optional.absent();
		// leave these null so if not set get an error in VesselPosition
		// constructor
		private Optional<Double> cogDegrees;
		private Optional<Double> headingDegrees;
		private Optional<Double> speedMetresPerSecond;
		private Optional<String> positionAisNmea;
		private Optional<String> shipStaticAisNmea;
		private VesselClass cls;
		private long time;
		private Optional<Integer> shipType = Optional.absent();
		private NavigationalStatus navigationalStatus;

		private Builder() {
		}

		public Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		public Builder lat(double lat) {
			this.lat = lat;
			return this;
		}

		public Builder lon(double lon) {
			this.lon = lon;
			return this;
		}

		public Builder lengthMetres(Optional<Integer> lengthMetres) {
			this.lengthMetres = lengthMetres;
			return this;
		}

		public Builder widthMetres(Optional<Integer> widthMetres) {
			this.widthMetres = widthMetres;
			return this;
		}

		public Builder cogDegrees(Optional<Double> cog) {
			this.cogDegrees = cog;
			return this;
		}

		public Builder headingDegrees(Optional<Double> heading) {
			this.headingDegrees = heading;
			return this;
		}

		public Builder speedMetresPerSecond(
				Optional<Double> speedMetresPerSecond) {
			this.speedMetresPerSecond = speedMetresPerSecond;
			return this;
		}

		public Builder time(long time) {
			this.time = time;
			return this;
		}

		public Builder cls(VesselClass cls) {
			this.cls = cls;
			return this;
		}

		public Builder shipType(Optional<Integer> shipType) {
			this.shipType = shipType;
			return this;
		}

		public Builder positionAisNmea(Optional<String> nmea) {
			this.positionAisNmea = nmea;
			return this;
		}

		public Builder shipStaticAisNmea(Optional<String> nmea) {
			this.shipStaticAisNmea = nmea;
			return this;
		}
		
		public Builder navigationalStatus(NavigationalStatus status) {
			this.navigationalStatus= status;
			return this;
		}

		public VesselPosition build() {
			return new VesselPosition(counter.incrementAndGet(), id, lat, lon,
					lengthMetres, widthMetres, cogDegrees, headingDegrees,
					speedMetresPerSecond, cls, navigationalStatus, time, shipType,
					positionAisNmea, shipStaticAisNmea);
		}

	}

	private double metresPerDegreeLongitude() {
		return Math.PI / 180 * EARTH_RADIUS_KM * Math.cos(toRadians(lat));
	}

	private double metresPerDegreeLatitude() {
		return 111321.543;
	}

	// private Area createArea(VesselPosition relativeTo) {
	// Vector v = position(relativeTo);
	// Rectangle2D.Double r = baseRectangle();
	// Area a = new Area(r);
	// AffineTransform af = new AffineTransform();
	// af.rotate(toRadians(headingDegrees), v.x(), v.y());
	// return a.createTransformedArea(af);
	// }

	public Vector position(VesselPosition relativeTo) {
		// TODO longitude wrapping check
		double xMetres = (lon - relativeTo.lon())
				* relativeTo.metresPerDegreeLongitude();
		double yMetres = (lat - relativeTo.lat())
				* relativeTo.metresPerDegreeLatitude();
		return new Vector(xMetres, yMetres);
	}

	// public boolean intersects(VesselPosition p) {
	// Area area = createArea(this);
	// area.intersect(p.createArea(this));
	// return !area.isEmpty();
	// }

	public Optional<VesselPosition> predict(long t) {
		if (!speedMetresPerSecond.isPresent() || !cogDegrees.isPresent()
				|| navigationalStatus == NavigationalStatus.AT_ANCHOR|| navigationalStatus == NavigationalStatus.MOORED)
			return Optional.absent();
		else {
			double lat = this.lat - speedMetresPerSecond.get()
					/ metresPerDegreeLatitude() * (t - time) / 1000.0
					* Math.cos(Math.toRadians(cogDegrees.get()));
			double lon = this.lon + speedMetresPerSecond.get()
					/ metresPerDegreeLongitude() * (t - time) / 1000.0
					* Math.sin(Math.toRadians(cogDegrees.get()));

			return Optional.of(new VesselPosition(messageId, id, lat, lon,
					lengthMetres, widthMetres, cogDegrees, headingDegrees,
					speedMetresPerSecond, cls, navigationalStatus, time, shipType,
					positionAisNmea, shipStaticAisNmea));
		}
	}

	private Optional<Vector> velocity() {
		if (speedMetresPerSecond.isPresent() && cogDegrees.isPresent())
			return Optional.of(new Vector(speedMetresPerSecond.get()
					* Math.sin(Math.toRadians(cogDegrees.get())),
					speedMetresPerSecond.get()
							* Math.cos(Math.toRadians(cogDegrees.get()))));
		else
			return Optional.absent();
	}

	/**
	 * Returns absent if no intersection occurs else return the one or two times
	 * of intersection of circles around the vessel relative to this.time().
	 * 
	 * @param vp
	 * @return
	 */
	public Optional<Times> intersectionTimes(VesselPosition vp) {

		// TODO handle vp doesn't have speed or cog but is within collision
		// distance given any cog and max speed

		Optional<VesselPosition> p = vp.predict(time);
		Vector deltaV = velocity().get().minus(p.get().velocity().get());
		Vector deltaP = position(this).minus(p.get().position(this));

		// imagine a ring around the vessel centroid with maxDimensionMetres/2
		// radius. This is the ring we are going to test for collision.
		double r = p.get().maxDimensionMetres()
				.or(maxDimensionMetresWhenUnknown)
				/ 2
				+ maxDimensionMetres().or(maxDimensionMetresWhenUnknown)
				/ 2;

		if (deltaP.dot(deltaP) <= r)
			return of(new Times(p.get().time()));

		double a = deltaV.dot(deltaV);
		double b = 2 * deltaV.dot(deltaP);
		double c = deltaP.dot(deltaP) - r * r;

		// Now solve the quadratic equation with coefficients a,b,c
		double discriminant = b * b - 4 * a * c;

		if (a == 0)
			return Optional.absent();
		else if (discriminant < 0)
			return Optional.absent();
		else {
			if (discriminant == 0) {
				return of(new Times(Math.round(-b / 2 / a)));
			} else {
				long alpha1 = Math
						.round((-b + Math.sqrt(discriminant)) / 2 / a);
				long alpha2 = Math
						.round((-b - Math.sqrt(discriminant)) / 2 / a);
				return of(new Times(alpha1, alpha2));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("VesselPosition [lat=");
		b.append(lat);
		b.append(", lon=");
		b.append(lon);
		b.append(", lengthMetres=");
		b.append(lengthMetres);
		b.append(", widthMetres=");
		b.append(widthMetres);
		b.append(", cogDegrees=");
		b.append(cogDegrees);
		b.append(", headingDegrees=");
		b.append(headingDegrees);
		b.append(", speedMetresPerSecond=");
		b.append(speedMetresPerSecond);
		b.append(", positionAisNmea=");
		b.append(positionAisNmea);
		b.append(", shipStaticAisNmea=");
		b.append(shipStaticAisNmea);
		b.append(", navStatus=");
		b.append(navigationalStatus);
		b.append(", time=");
		b.append(time);
		b.append(", id=");
		b.append(id);
		b.append(", cls=");
		b.append(cls);
		b.append(", shipType=");
		b.append(shipType);
		b.append(", messageId=");
		b.append(messageId);
		b.append("]");
		return b.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (messageId ^ (messageId >>> 32));
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VesselPosition other = (VesselPosition) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (messageId != other.messageId)
			return false;
		if (time != other.time)
			return false;
		return true;
	}

}
