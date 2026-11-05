"use client";

import Footer from "../components/Footer";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import { useSelector } from "react-redux";
import type { RootState } from "../redux/store";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function SistemaLayout({ children }: { children: React.ReactNode }) {
    const usuario = useSelector((state: RootState) => state.auth.usuario);
    const router = useRouter();

    useEffect(() => {
        if (usuario == null) {
            router.replace("/login");
        }
    }, [usuario, router]);

    if (usuario == null) return null;

    return (
        <div className="flex min-h-screen bg-purple-50">
            <Sidebar />
            <div className="flex min-w-0 flex-1 flex-col overflow-hidden">
                <Header />
                <main className="flex-1 overflow-y-auto bg-purple-50">{children}</main>
                <Footer />
            </div>
        </div>
    );
}
