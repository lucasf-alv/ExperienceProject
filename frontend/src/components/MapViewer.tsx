import { MapContainer, Marker, TileLayer } from "react-leaflet";
import L from "leaflet";

const icon = new L.Icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

interface Props {
  latitude: number;
  longitude: number;
}

export function MapViewer({ latitude, longitude }: Props) {
  return (
    <MapContainer
      center={[latitude, longitude]}
      zoom={15}
      className="h-72 w-full rounded-xl"
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

      <Marker position={[latitude, longitude]} icon={icon} />
    </MapContainer>
  );
}
