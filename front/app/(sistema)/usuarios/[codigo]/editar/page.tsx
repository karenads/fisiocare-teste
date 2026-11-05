"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import type { Usuario } from "@/app/types/usuario";
import { consultaUsuarioPorId } from "@/app/services/usuarioService";
import UsuarioForm from "@/app/(sistema)/usuarios/components/UsuarioForm";

export default function EditarUsuario() {
    const parametro = useParams();
    const codigo = Number(parametro.codigo);
    const router = useRouter();
    const [dados, setDados] = useState<Usuario | null>(null);

    useEffect(() => {
        consultaUsuarioPorId(codigo).then(setDados).catch(() => {
            alert("Registro não encontrado ou sem permissão!");
            router.push("/usuarios");
        });
    }, [codigo, router]);

    if (!dados) return <p className="p-8 text-purple-800">Carregando dados...</p>;

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-4xl rounded-xl bg-white p-8">
                <Link href="/usuarios" className="text-purple-700">Voltar para listagem</Link>
                <h1 className="my-6 text-3xl font-bold text-purple-800">Editar Usuario {codigo}</h1>
                <UsuarioForm usuarioExistente={dados} />
            </div>
        </div>
    );
}
