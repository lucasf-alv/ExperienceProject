import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import { useState } from "react";

interface Props {
  latitude: number;
  longitude: number;
  editable?: boolean;
  onLocationChange?: (lat: number, lng: number) => void;
}

function LocationSelector({
  onLocationChange,
}: {
  onLocationChange: (lat: number, lng: number) => void;
}) {
  useMapEvents({
    click(e) {
      onLocationChange(e.latlng.lat, e.latlng.lng);
    },
  });

  return null;
}

export function MapViewer({
  latitude,
  longitude,
  editable = false,
  onLocationChange,
}: Props) {
  const [position, setPosition] = useState({
    lat: latitude,
    lng: longitude,
  });

  function handleChange(lat: number, lng: number) {
    setPosition({
      lat,
      lng,
    });

    onLocationChange?.(lat, lng);
  }

  return (
    <MapContainer
      center={[position.lat, position.lng]}
      zoom={15}
      className="h-80 w-full rounded-xl"
    >
      <TileLayer url="https://tile.openstreetmap.org/{z}/{x}/{y}.png" />

      <Marker position={[position.lat, position.lng]} />

      {editable && onLocationChange && (
        <LocationSelector onLocationChange={handleChange} />
      )}
    </MapContainer>
  );
}
