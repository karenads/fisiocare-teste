"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import type { Paciente } from "@/app/types/paciente";
import { buscarPacientePorId } from "@/app/services/pacienteService";
import PacienteForm from "@/app/(sistema)/pacientes/components/PacienteForm";

export default function EditarPaciente() {
    const parametro = useParams();
    const codigo = Number(parametro.codigo);
    const router = useRouter();
    const [dados, setDados] = useState<Paciente | null>(null);

    useEffect(() => {
        buscarPacientePorId(codigo).then(setDados).catch(() => {
            alert("Registro não encontrado ou sem permissão!");
            router.push("/pacientes");
        });
    }, [codigo, router]);

    if (!dados) return <p className="p-8 text-purple-800">Carregando dados...</p>;

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-4xl rounded-xl bg-white p-8">
                <Link href="/pacientes" className="text-purple-700">Voltar para listagem</Link>
                <h1 className="my-6 text-3xl font-bold text-purple-800">Editar Paciente {codigo}</h1>
                <PacienteForm pacienteExistente={dados} />
            </div>
        </div>
    );
}
