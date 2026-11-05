"use client";

import Link from "next/link";
import { useSelector } from "react-redux";
import type { RootState } from "@/app/redux/store";

export default function Sidebar() {
    const usuario = useSelector((state: RootState) => state.auth.usuario);
    const role = usuario?.role;

    return (
        <aside className="min-h-screen w-64 border-r border-purple-200 bg-purple-900 p-6 shadow-lg">
            <h1 className="mb-8 text-xl font-bold text-purple-100">FisioCare</h1>
            <nav className="flex flex-col gap-2">
                <Link href="/home" className="rounded-xl px-4 py-3 text-purple-100 hover:bg-purple-800">Home</Link>
                <Link href="/pacientes" className="rounded-xl px-4 py-3 text-purple-100 hover:bg-purple-800">Pacientes</Link>
                {(role === "ROLE_ADMIN" || role === "ROLE_FISIOTERAPEUTA") && (
                    <>
                        <Link href="/tratamentos" className="rounded-xl px-4 py-3 text-purple-100 hover:bg-purple-800">Tratamentos</Link>
                        <Link href="/sessoes" className="rounded-xl px-4 py-3 text-purple-100 hover:bg-purple-800">Sessões</Link>
                    </>
                )}
                {role === "ROLE_ADMIN" && (
                    <Link href="/usuarios" className="rounded-xl px-4 py-3 text-purple-100 hover:bg-purple-800">Usuários</Link>
                )}
            </nav>
        </aside>
    );
}
