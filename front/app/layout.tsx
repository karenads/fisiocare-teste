import type { Metadata } from "next";
import "./globals.css";
import StoreProvider from "./redux/StoreProvider";

export const metadata: Metadata = {
    title: "FisioCare",
    description: "Gerenciador de fisioterapia"
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="pt-BR">
            <body>
                <StoreProvider>{children}</StoreProvider>
            </body>
        </html>
    );
}
