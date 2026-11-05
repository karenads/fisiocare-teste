"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import type { Sessao } from "@/app/types/sessao";
import { buscarSessaoPorId } from "@/app/services/sessaoService";
import SessaoForm from "@/app/(sistema)/sessoes/components/SessaoForm";

export default function EditarSessao() {
    const parametro = useParams();
    const codigo = Number(parametro.codigo);
    const router = useRouter();
    const [dados, setDados] = useState<Sessao | null>(null);

    useEffect(() => {
        buscarSessaoPorId(codigo).then(setDados).catch(() => {
            alert("Registro não encontrado ou sem permissão!");
            router.push("/sessoes");
        });
    }, [codigo, router]);

    if (!dados) return <p className="p-8 text-purple-800">Carregando dados...</p>;

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-4xl rounded-xl bg-white p-8">
                <Link href="/sessoes" className="text-purple-700">Voltar para listagem</Link>
                <h1 className="my-6 text-3xl font-bold text-purple-800">Editar Sessao {codigo}</h1>
                <SessaoForm sessaoExistente={dados} />
            </div>
        </div>
    );
}
