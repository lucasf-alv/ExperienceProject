import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";

import { useState } from "react";

interface Props {
  onLocationChange: (latitude: number, longitude: number) => void;
}

function LocationMarker({ onLocationChange }: Props) {
  const [position, setPosition] = useState<[number, number] | null>(null);

  useMapEvents({
    click(e) {
      const lat = e.latlng.lat;
      const lng = e.latlng.lng;

      setPosition([lat, lng]);

      onLocationChange(lat, lng);
    },
  });

  return position ? <Marker position={position} /> : null;
}

export function MapPicker({ onLocationChange }: Props) {
  return (
    <MapContainer
      center={[-15.7942, -47.8822]}
      zoom={13}
      className="h-72 w-full rounded-xl"
    >
      <TileLayer url="https://tile.openstreetmap.org/{z}/{x}/{y}.png" />

      <LocationMarker onLocationChange={onLocationChange} />
    </MapContainer>
  );
}
